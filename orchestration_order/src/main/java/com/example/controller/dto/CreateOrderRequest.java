package com.example.controller.dto;

import com.example.application.dto.CreateOrderCommand;

import java.util.List;

public record CreateOrderRequest(
        List<OrderItem> orderItems
) {

    public CreateOrderCommand toCommand() {
        return new CreateOrderCommand(
                orderItems.stream()
                        .map(item -> new CreateOrderCommand.OrderItem(item.productId, item.quantity))
                        .toList()
        );
    }

    public record OrderItem(
            Long productId,
            Long quantity
    ) {
    }
}
