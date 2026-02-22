package com.example.controller;

import com.example.application.OrderCoordinator;
import com.example.application.OrderService;
import com.example.application.RedisLockService;
import com.example.application.dto.CreateOrderResult;
import com.example.controller.dto.CreateOrderRequest;
import com.example.controller.dto.CreateOrderResponse;
import com.example.controller.dto.PlaceOrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final RedisLockService redisLockService;

    @PostMapping("/order")
    public CreateOrderResponse createOrder(@RequestBody CreateOrderRequest request) {
        CreateOrderResult result = orderService.createOrder(request.toCommand());
        return new CreateOrderResponse(result.orderId());
    }

    @PostMapping("/order/place")
    public void placeOrder(@RequestBody PlaceOrderRequest request) {

        String key = "order:" + request.orderId();
        boolean acquiredLock = redisLockService.tryLock(key, request.orderId().toString());

        if (!acquiredLock) {
            System.out.println("락 획득에 실패하였습니다.");
            throw new RuntimeException("락 획득에 실패하였습니다.");
        }

        try {
            orderService.placeOrder(request.toCommand());
        } finally {
            redisLockService.releaseLock(key);
        }
    }
}
