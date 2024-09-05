package com.github.sanjayrawat1.domain;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * A dead letter topic record.
 *
 * @author sanjayrawat1
 */
@Setter
@Getter
@Entity
@Table(name = "dead_letter_topic_record")
public class DeadLetterTopicRecord extends AbstractAuditingEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -7218150661571307799L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Trace id of original kafka listener.
     */
    @Column(name = "trace_id")
    private String traceId;

    /**
     * Original topic name.
     */
    private String topic;

    /**
     * Kafka listener payload.
     */
    private String payload;

    /**
     * Exception recorded while processing original kafka listener.
     */
    private String exception;
}
