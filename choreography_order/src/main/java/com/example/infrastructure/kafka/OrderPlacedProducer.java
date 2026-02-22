package com.example.infrastructure.kafka;

import com.example.infrastructure.kafka.dto.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderPlacedProducer {

    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public void send(OrderPlacedEvent event) {
        kafkaTemplate.send(
                "oder-placed",
                event.orderId().toString(),
                event
        );
    }
}
