package com.github.sanjayrawat1.service.messaging.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @author sanjayrawat1
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, Object data) {
        log.info("Send data : '{}' to topic : '{}'", data, topic);
        kafkaTemplate.send(topic, data);
    }

    public void send(String topic, String key, Object data) {
        log.info("Send data : '{}' to topic : '{}' with key : {}", data, topic, key);
        kafkaTemplate.send(topic, key, data);
    }
}
