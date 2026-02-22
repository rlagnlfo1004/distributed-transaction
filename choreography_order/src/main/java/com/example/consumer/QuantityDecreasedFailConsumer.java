package com.example.consumer;

import com.example.application.OrderService;
import com.example.consumer.dto.QuantityDecreasedFailEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuantityDecreasedFailConsumer {

    private final OrderService orderService;

    @KafkaListener(
            topics = "quantity-decreased-fail",
            groupId = "quantity-decreased-fail-consumer",
            properties = {
                    "spring.json.value.default.type=com.example.consumer.dto.QuantityDecreasedFailEvent"
            }
    )
    public void handle(QuantityDecreasedFailEvent event) {
        orderService.fail(event.orderId());
    }
}
