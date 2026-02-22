package com.example.consumer;

import com.example.application.PointService;
import com.example.application.dto.PointUseCancelCommand;
import com.example.application.dto.PointUseCommand;
import com.example.consumer.dto.QuantityDecreasedEvent;
import com.example.infrastructure.kafka.PointUsedFailProducer;
import com.example.infrastructure.kafka.PointUsedProducer;
import com.example.infrastructure.kafka.dto.PointUsedEvent;
import com.example.infrastructure.kafka.dto.PointUsedFailEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuantityDecreasedConsumer {

    private final PointService pointService;
    private final PointUsedProducer pointUsedProducer;
    private final PointUsedFailProducer pointUsedFailProducer;

    @KafkaListener(
            topics = "quantity-decreased",
            groupId = "quantity-decreased-consumer",
            properties = {
                    "spring.json.value.default.type=com.example.consumer.dto.QuantityDecreasedEvent"
            }
    )
    public void handle(QuantityDecreasedEvent event) {
        String requestId = event.orderId().toString();
        try {
            pointService.use(new PointUseCommand(requestId, 1L, event.totalPrice()));

            pointUsedProducer.send(new PointUsedEvent(event.orderId()));
        } catch (Exception e) {
            pointService.cancel(new PointUseCancelCommand(requestId));

            pointUsedFailProducer.send(new PointUsedFailEvent(event.orderId()));
        }
    }
}
