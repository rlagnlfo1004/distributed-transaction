package com.example.infrastructure.kafka;

import com.example.infrastructure.kafka.dto.PointUsedFailEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointUsedFailProducer {

    private final KafkaTemplate<String, PointUsedFailEvent> kafkaTemplate;

    public void send(PointUsedFailEvent event) {
        kafkaTemplate.send(
                "point-used-fail",
                event.orderId().toString(),
                event
        );
    }
}
