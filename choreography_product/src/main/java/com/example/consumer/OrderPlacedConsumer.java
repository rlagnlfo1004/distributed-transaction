package com.example.consumer;

import com.example.application.ProductService;
import com.example.application.dto.ProductBuyCancelCommand;
import com.example.application.dto.ProductBuyCommand;
import com.example.application.dto.ProductBuyResult;
import com.example.consumer.dto.OrderPlacedEvent;
import com.example.infrastructure.kafka.QuantityDecreasedFailProducer;
import com.example.infrastructure.kafka.QuantityDecreasedProducer;
import com.example.infrastructure.kafka.dto.QuantityDecreasedEvent;
import com.example.infrastructure.kafka.dto.QuantityDecreasedFailEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderPlacedConsumer {

    private final ProductService productService;
    private final QuantityDecreasedProducer quantityDecreasedProducer;
    private final QuantityDecreasedFailProducer quantityDecreasedFailProducer;

    @KafkaListener(
            topics = "order-placed",
            groupId = "order-placed-consumer",
            properties = {
                    "spring.json.value.default.type=com.example.consumer.dto.OrderPlacedEvent"
            }
    )
    public void handle(OrderPlacedEvent event) {
        String requestId = event.orderId().toString();

        try {
            ProductBuyResult result = productService.buy(
                    new ProductBuyCommand(
                            requestId,
                            event.productInfos().stream()
                                    .map(orderItem -> new ProductBuyCommand.ProductInfo(orderItem.productId(), orderItem.quantity()))
                                    .toList()
                    )
            );

            quantityDecreasedProducer.send(
                    new QuantityDecreasedEvent(
                            event.orderId(),
                            result.totalPrice()
                    )
            );
        } catch (Exception e) {
            productService.cancel(
                    new ProductBuyCancelCommand(requestId)
            );

            quantityDecreasedFailProducer.send(
                    new QuantityDecreasedFailEvent(event.orderId())
            );
        }
    }
}
