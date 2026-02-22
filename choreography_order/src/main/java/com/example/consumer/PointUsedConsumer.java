package com.example.consumer;

import com.example.application.OrderService;
import com.example.consumer.dto.PointUsedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointUsedConsumer {

    private final OrderService orderService;

    @KafkaListener(
            topics = "point-used",
            groupId = "point-used-consumer",
            properties = {
                    "spring.json.value.default.type=com.example.consumer.dto.PointUsedEvent"
            }
    )
    public void handle(PointUsedEvent event) {
        orderService.complete(event.orderId());
    }
}
