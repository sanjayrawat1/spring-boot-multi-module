package com.github.sanjayrawat1.service.messaging.kafka.payload;

import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Order status listener pushed from Order Service.
 *
 * @author sanjayrawat1
 */
@Getter
@Setter
@ToString
public class OrderStatusPayload implements Serializable {

    @Serial
    private static final long serialVersionUID = 6130679949373395995L;

    private Long orderId;

    private Long userId;

    private String clientId;

    private OrderStatus status;

    public enum OrderStatus {
        CANCELLED,
        COMPLETED,
        ERROR,
        FULFILLED,
        IN_PROGRESS,
        INSTALLING,
        PENDING,
        PREPARING,
        PROCESSING,
        RECEIVED,
    }
}
