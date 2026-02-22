package com.example.consumer.dto;

public record QuantityDecreasedEvent(Long orderId, Long totalPrice) {
}
