package com.example.infrastructure.point;

public record PointUseApiRequest(
        String requestId,
        Long userId,
        Long amount
) {
}
