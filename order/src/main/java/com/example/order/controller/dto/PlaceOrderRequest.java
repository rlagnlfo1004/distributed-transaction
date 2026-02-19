package com.example.order.controller.dto;

import com.example.order.application.dto.PlaceOrderCommand;

public record PlaceOrderRequest(
        Long orderId
) {
    public PlaceOrderCommand toPlaceOrderCommand() {
        return new PlaceOrderCommand(orderId);
    }
}
