package com.github.sanjayrawat1.config;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.util.ObjectUtils;

/**
 * The {@link org.springframework.kafka.support.ProducerListener} that logs success info after it has been
 * acknowledged by the broker and exceptions thrown when sending messages.
 *
 * @author sanjayrawat1
 */
@Slf4j
@Setter
public class LoggingProducerListener<K, V> extends org.springframework.kafka.support.LoggingProducerListener<K, V> {

    private int maxContentLogged = DEFAULT_MAX_CONTENT_LOGGED;

    @Override
    public void onSuccess(ProducerRecord<K, V> record, RecordMetadata metadata) {
        log.info(
            "Sent message with key : '{}' and payload : '{}' to topic : {} and partition : {} at offset : {}",
            toDisplayString(ObjectUtils.nullSafeToString(record.key()), maxContentLogged),
            toDisplayString(ObjectUtils.nullSafeToString(record.value()), maxContentLogged),
            record.topic(),
            record.partition(),
            metadata.offset()
        );
    }

    private String toDisplayString(String original, int maxCharacters) {
        if (original.length() <= maxCharacters) {
            return original;
        }
        return original.substring(0, maxCharacters) + "...";
    }
}
