package com.github.sanjayrawat1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.ProducerListener;

/**
 * Kafka Producer Configuration.
 *
 * @author sanjayrawat1
 */
@Configuration
public class KafkaProducerConfiguration {

    @Bean
    public ProducerListener<Object, Object> kafkaProducerListener() {
        return new LoggingProducerListener<>();
    }
}
