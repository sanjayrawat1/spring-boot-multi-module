package com.github.sanjayrawat1.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sanjayrawat1.errors.BadRequestException;
import com.github.sanjayrawat1.errors.ResourceNotFoundException;
import java.util.Set;
import java.util.function.BiFunction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.converter.ByteArrayJsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.util.backoff.ExponentialBackOff;

/**
 * Kafka Consumer Configuration.
 *
 * @author sanjayrawat1
 */
@Slf4j
@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfiguration {

    public static final String ORDER_STATUS_KAFKA_LISTENER_CONTAINER_FACTORY = "orderStatusKafkaListenerContainerFactory";

    private static final BiFunction<ConsumerRecord<?, ?>, Exception, TopicPartition> DEFAULT_DESTINATION_RESOLVER = (consumerRecord, exception) ->
        new TopicPartition(consumerRecord.topic() + KafkaTopicConfiguration.DEAD_LETTER_TOPIC_SUFFIX, -1);

    private static final Set<Class<? extends Exception>> NON_RETRYABLE_EXCEPTIONS = Set.of(BadRequestException.class, ResourceNotFoundException.class);

    private final ObjectMapper objectMapper;

    private final KafkaProperties kafkaProperties;

    private final ApplicationProperties applicationProperties;

    @Bean
    public SeekToCurrentErrorHandler errorHandler(DeadLetterPublishingRecoverer deadLetterPublishingRecoverer) {
        var exponentialBackOff = applicationProperties.getKafka().getConsumer().getErrorHandler().getExponentialBackOff();
        var backOff = new ExponentialBackOff(exponentialBackOff.getInitialInterval(), exponentialBackOff.getMultiplier());
        backOff.setMaxInterval(exponentialBackOff.getMaxInterval());
        backOff.setMaxElapsedTime(exponentialBackOff.getMaxElapsedTime());
        var errorHandler = new SeekToCurrentErrorHandler(deadLetterPublishingRecoverer, backOff);
        NON_RETRYABLE_EXCEPTIONS.forEach(errorHandler::addNotRetryableExceptions);
        return errorHandler;
    }

    @Bean
    public DeadLetterPublishingRecoverer publisher(KafkaOperations<?, ?> kafkaTemplate) {
        return new DeadLetterPublishingRecoverer(kafkaTemplate, DEFAULT_DESTINATION_RESOLVER);
    }

    @Bean
    public RecordMessageConverter converter() {
        return new ByteArrayJsonMessageConverter();
    }

    /**
     * Container factory for order status kafka listener. Build using global configurations provided in yaml file.
     *
     * @param configurer listener container factory configurer.
     * @param kafkaConsumerFactory kafka consumer factory.
     * @return container factory for order status listener.
     */
    @Bean(ORDER_STATUS_KAFKA_LISTENER_CONTAINER_FACTORY)
    public ConcurrentKafkaListenerContainerFactory<?, ?> orderStatusKafkaListenerContainerFactory(
        ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
        ObjectProvider<ConsumerFactory<Object, Object>> kafkaConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setRecordFilterStrategy(this::orderStatusRecordFilterStrategy);
        configurer.configure(
            factory,
            kafkaConsumerFactory.getIfAvailable(() -> new DefaultKafkaConsumerFactory<>(this.kafkaProperties.buildConsumerProperties()))
        );
        return factory;
    }

    /**
     * Filter record that are not meant to be processed by our service(filtered using clientId).
     * Producer will send the payload with the clientId.
     *
     * @param record record that needs to be consumed by the kafka listener.
     * @return return boolean
     */
    private boolean orderStatusRecordFilterStrategy(ConsumerRecord<Object, Object> record) {
        try {
            JsonNode clientId = objectMapper.readValue(record.value().toString(), JsonNode.class).get("clientId");
            return !applicationProperties.getClientId().equalsIgnoreCase(clientId != null ? clientId.asText() : null);
        } catch (JsonProcessingException e) {
            log.warn("Invalid order status consumer record : {}", record, e);
        }
        return true;
    }
}
