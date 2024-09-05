package com.github.sanjayrawat1.service.messaging.kafka.consumer;

import static com.github.sanjayrawat1.config.KafkaConsumerConfiguration.ORDER_STATUS_KAFKA_LISTENER_CONTAINER_FACTORY;
import static com.github.sanjayrawat1.config.KafkaTopicConfiguration.DEAD_LETTER_TOPIC_SUFFIX;
import static com.github.sanjayrawat1.service.messaging.kafka.KafkaConstants.DEAD_LETTER_TOPIC_GROUP;

import com.github.sanjayrawat1.security.SecurityUtils;
import com.github.sanjayrawat1.service.messaging.kafka.payload.OrderStatusPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @author sanjayrawat1
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderStatusListener extends DeadLetterTopicRecordProcessor {

    @KafkaListener(
        autoStartup = "${application.kafka.topic.order-status.enabled:false}",
        topics = "${application.kafka.topic.order-status.topic-name}",
        containerFactory = ORDER_STATUS_KAFKA_LISTENER_CONTAINER_FACTORY
    )
    public void listen(ConsumerRecord<String, Object> record, @Payload OrderStatusPayload payload) {
        addTraceIdHeaderIfRequired(record);
        SecurityUtils.createAuthenticationForSystem(payload.getUserId());
        log.info("Processing order status event for the payload : {}", payload);
    }

    @KafkaListener(
        autoStartup = "${application.kafka.topic.order-status.enabled:false}",
        topics = "${application.kafka.topic.order-status.topic-name}" + DEAD_LETTER_TOPIC_SUFFIX,
        groupId = DEAD_LETTER_TOPIC_GROUP
    )
    public void dltListen(ConsumerRecord<String, Object> record) {
        createDeadLetterTopicRecord(record);
    }
}
