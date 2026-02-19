package com.example.application;

import com.example.application.dto.CreateOrderCommand;
import com.example.application.dto.CreateOrderResult;
import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.infrastructure.OrderItemRepository;
import com.example.infrastructure.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public CreateOrderResult createOrder(CreateOrderCommand command) {
        Order order = orderRepository.save(new Order());

        List<OrderItem> orderItems = command.items()
                .stream()
                .map(item -> new OrderItem(order.getId(), item.productId(), item.quantity()))
                .toList();

        orderItemRepository.saveAll(orderItems);
        return new CreateOrderResult(order.getId());
    }
}
