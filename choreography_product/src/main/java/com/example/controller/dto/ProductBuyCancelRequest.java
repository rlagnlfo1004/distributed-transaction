package com.example.controller.dto;

import com.example.application.dto.ProductBuyCancelCommand;

public record ProductBuyCancelRequest(String requestId) {

    public ProductBuyCancelCommand toCommand() {
        return new ProductBuyCancelCommand(requestId);
    }
}
