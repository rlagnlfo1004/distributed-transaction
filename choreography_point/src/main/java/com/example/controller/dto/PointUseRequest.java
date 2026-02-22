package com.example.controller.dto;

import com.example.application.dto.PointUseCommand;

public record PointUseRequest(
        String requestId,
        Long userId,
        Long amount
) {

    public PointUseCommand toCommand() {
        return new PointUseCommand(requestId, userId, amount);
    }
}
