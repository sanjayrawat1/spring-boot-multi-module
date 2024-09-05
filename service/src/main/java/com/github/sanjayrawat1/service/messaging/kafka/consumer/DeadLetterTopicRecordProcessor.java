package com.github.sanjayrawat1.service.messaging.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sanjayrawat1.config.ApplicationConstants;
import com.github.sanjayrawat1.domain.DeadLetterTopicRecord;
import com.githun.sanjayrawat1.persistence.repository.DeadLetterTopicRecordRepository;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.KafkaHeaders;

/**
 * Common super class for classes that deals with listening data of dead letter topic.
 *
 * @author sanjayrawat1
 */
@Slf4j
public abstract class DeadLetterTopicRecordProcessor {

    private static final String ORIGINAL_TRACE_ID_KEY = "originalTraceId";

    private ObjectMapper objectMapper;

    private DeadLetterTopicRecordRepository deadLetterTopicRecordRepository;

    @Autowired
    public final void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public final void setDeadLetterTopicRecordRepository(DeadLetterTopicRecordRepository deadLetterTopicRecordRepository) {
        this.deadLetterTopicRecordRepository = deadLetterTopicRecordRepository;
    }

    /**
     * @see <a href="https://github.com/spring-projects/spring-kafka/issues/1704">Missing trace information in Kafka Error Handler</a>
     * <p>
     * Once this bug/enhancement is resolved, we will check if error handler traceId is propagated in dead letter topic listener,
     * if yes then instead of setting original listener trace id in the consumer record header, we will get one from the MDC.
     * After verifying the propagation is heppening, remove this method.
     *
     * @param record the consumer record.
     */
    protected final void addTraceIdHeaderIfRequired(ConsumerRecord<String, Object> record) {
        Header originalTraceIdHeader = record.headers().lastHeader(ORIGINAL_TRACE_ID_KEY);
        if (originalTraceIdHeader == null) {
            record.headers().add(ORIGINAL_TRACE_ID_KEY, MDC.get(ApplicationConstants.SLEUTH_TRACE_ID_KEY).getBytes(StandardCharsets.UTF_8));
        }
    }

    /**
     * Store failed record with cause in the database.
     *
     * @param record the consumer record.
     */
    protected void createDeadLetterTopicRecord(ConsumerRecord<String, Object> record) {
        DeadLetterTopicRecord deadLetterTopicRecord = new DeadLetterTopicRecord();

        // Once we are ready to remove the DeadLetterTopicProcessor#addTraceIdHeaderIfRequired() method,
        // then also remove below code of getting original trace id from header, instead we will retrieve one from the MDC.
        Header parentTraceIdHeader = record.headers().lastHeader(ORIGINAL_TRACE_ID_KEY);
        if (parentTraceIdHeader != null) {
            deadLetterTopicRecord.setTraceId(new String(parentTraceIdHeader.value()));
        } else {
            log.warn("Original trace id not found. Setting current traceId");
            deadLetterTopicRecord.setTraceId(MDC.get(ApplicationConstants.SLEUTH_TRACE_ID_KEY));
        }

        Header originalTopicHeader = record.headers().lastHeader(KafkaHeaders.DLT_ORIGINAL_TOPIC);
        if (originalTopicHeader != null) {
            deadLetterTopicRecord.setTopic(new String(originalTopicHeader.value()));
        }

        Header exceptionMessageHeader = record.headers().lastHeader(KafkaHeaders.DLT_EXCEPTION_MESSAGE);
        if (exceptionMessageHeader != null) {
            deadLetterTopicRecord.setException(new String(exceptionMessageHeader.value()));
        } else {
            log.warn("DLT exception stacktrace : {}", new String(record.headers().lastHeader(KafkaHeaders.DLT_EXCEPTION_STACKTRACE).value()));
        }

        try {
            JsonNode failedRecordValue = objectMapper.readValue(record.value().toString(), JsonNode.class);
            if (failedRecordValue != null) {
                deadLetterTopicRecord.setPayload(failedRecordValue.asText());
            }
        } catch (JsonProcessingException e) {
            log.warn("Unable to process/read record : {}", record.value(), e);
            deadLetterTopicRecord.setPayload(record.value().toString());
        }

        deadLetterTopicRecordRepository.save(deadLetterTopicRecord);
    }
}
