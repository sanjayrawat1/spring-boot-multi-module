package com.github.sanjayrawat1.service.messaging.kafka.consumer;

import static com.github.sanjayrawat1.config.KafkaTopicConfiguration.DEAD_LETTER_TOPIC_SUFFIX;
import static com.github.sanjayrawat1.service.messaging.kafka.KafkaConstants.DEAD_LETTER_TOPIC_GROUP;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author sanjayrawat1
 */
@Slf4j
@Component
public class TestListener extends DeadLetterTopicRecordProcessor {

    @KafkaListener(autoStartup = "${application.kafka.topic.test.enabled:false}", topics = "${application.kafka.topic.test.topic-name}")
    public void listen(ConsumerRecord<String, Object> record) {
        addTraceIdHeaderIfRequired(record);
    }

    @KafkaListener(
        autoStartup = "${application.kafka.topic.test.enabled:false}",
        topics = "${application.kafka.topic.test.topic-name}" + DEAD_LETTER_TOPIC_SUFFIX,
        groupId = DEAD_LETTER_TOPIC_GROUP
    )
    public void dltListen(ConsumerRecord<String, Object> record) {
        createDeadLetterTopicRecord(record);
    }
}
