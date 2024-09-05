package com.github.sanjayrawat1.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Kafka Topic Configuration.
 * Add a new bean of type {@link NewTopic} to be picked and created by {@link org.springframework.kafka.core.KafkaAdmin}.
 *
 * @author sanjayrawat1
 */
@Configuration
@RequiredArgsConstructor
@Profile(ApplicationConstants.SPRING_PROFILE_DEVELOPMENT)
public class KafkaTopicConfiguration {

    public static final String DEAD_LETTER_TOPIC_SUFFIX = ".SPRING_BOOT_MULTI_MODULE.DLT";
    public static final Integer DEFAULT_DLT_PARTITIONS = 1;
    public static final Integer DEFAULT_DLT_REPLICAS = 1;

    private final ApplicationProperties applicationProperties;

    @Bean
    @ConditionalOnProperty(name = "application.kafka.topic.test.enabled", havingValue = "true")
    public NewTopic testTopic() {
        var test = applicationProperties.getKafka().getTopic().getTest();
        return TopicBuilder.name(test.getTopicName()).partitions(test.getPartitions()).replicas(test.getReplicas()).build();
    }

    @Bean
    @ConditionalOnProperty(name = "application.kafka.topic.test.enabled", havingValue = "true")
    public NewTopic testDltTopic() {
        var test = applicationProperties.getKafka().getTopic().getTest();
        return TopicBuilder.name(test.getTopicName() + DEAD_LETTER_TOPIC_SUFFIX).partitions(DEFAULT_DLT_PARTITIONS).replicas(DEFAULT_DLT_REPLICAS).build();
    }

    @Bean
    @ConditionalOnProperty(name = "application.kafka.topic.order-status.enabled", havingValue = "true")
    public NewTopic orderStatusTopic() {
        var orderStatus = applicationProperties.getKafka().getTopic().getOrderStatus();
        return TopicBuilder.name(orderStatus.getTopicName()).partitions(orderStatus.getPartitions()).replicas(orderStatus.getReplicas()).build();
    }

    @Bean
    @ConditionalOnProperty(name = "application.kafka.topic.order-status.enabled", havingValue = "true")
    public NewTopic orderStatusDltTopic() {
        var orderStatus = applicationProperties.getKafka().getTopic().getOrderStatus();
        return TopicBuilder
            .name(orderStatus.getTopicName() + DEAD_LETTER_TOPIC_SUFFIX)
            .partitions(DEFAULT_DLT_PARTITIONS)
            .replicas(DEFAULT_DLT_REPLICAS)
            .build();
    }
}
