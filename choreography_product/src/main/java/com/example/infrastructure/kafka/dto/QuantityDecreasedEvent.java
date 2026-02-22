package com.example.infrastructure.kafka.dto;

public record QuantityDecreasedEvent(Long orderId, Long totalPrice) {
}
