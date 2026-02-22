package com.example.consumer;

import com.example.application.ProductService;
import com.example.application.dto.ProductBuyCancelCommand;
import com.example.consumer.dto.PointUsedFailEvent;
import com.example.infrastructure.kafka.QuantityDecreasedFailProducer;
import com.example.infrastructure.kafka.dto.QuantityDecreasedFailEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointUseFailConsumer {

    private final ProductService productService;
    private final QuantityDecreasedFailProducer quantityDecreasedFailProducer;


    @KafkaListener(
            topics = "point-used-fail",
            groupId = "point-used-fail-consumer",
            properties = {
                    "spring.json.value.default.type=com.example.consumer.dto.PointUsedFailEvent"
            }
    )
    public void handle(PointUsedFailEvent event) {
        productService.cancel(new ProductBuyCancelCommand(event.orderId().toString()));
        quantityDecreasedFailProducer.send(new QuantityDecreasedFailEvent(event.orderId()));
    }
}
