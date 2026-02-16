package com.example.monolithic.order.controller;

import com.example.monolithic.order.application.OrderService;
import com.example.monolithic.order.controller.dto.PlaceOrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order/place")
    public void placeOrder(@RequestBody PlaceOrderRequest request) {
        orderService.placeOrder(request.toPlaceOrderCommand());
    }
}
