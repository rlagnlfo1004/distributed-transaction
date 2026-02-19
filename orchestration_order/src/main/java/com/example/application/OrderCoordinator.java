package com.example.application;

import com.example.application.dto.OrderDto;
import com.example.application.dto.PlaceOrderCommand;
import com.example.infrastructure.point.PointApiClient;
import com.example.infrastructure.point.PointUseApiRequest;
import com.example.infrastructure.point.PointUseCancelApiRequest;
import com.example.infrastructure.product.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderCoordinator {

    private final OrderService orderService;
    private final PointApiClient pointApiClient;
    private final ProductApiClient productApiClient;

    public void placeOrder(PlaceOrderCommand command) {
        orderService.request(command.orderId());
        OrderDto orderDto = orderService.getOrder(command.orderId());

        try {
            ProductBuyApiRequest productBuyApiRequest = new ProductBuyApiRequest(
                    command.orderId().toString(),
                    orderDto.orderItems().stream()
                            .map(item -> new ProductBuyApiRequest.ProductInfo(item.productId(), item.quantity()
                            )).toList()
            );
            ProductBuyApiResponse productBuyApiResponse = productApiClient.buy(productBuyApiRequest);

            PointUseApiRequest pointUseApiRequest = new PointUseApiRequest(
                    command.orderId().toString(),
                    1L,
                    productBuyApiResponse.totalPrice()
            );
            pointApiClient.use(pointUseApiRequest);

            orderService.complete(command.orderId());
        } catch (Exception e) {
            ProductBuyCancelApiRequest productBuyCancelApiRequest = new ProductBuyCancelApiRequest(command.orderId().toString());
            ProductBuyCancelApiResponse productBuyCancelApiResponse = productApiClient.cancel(productBuyCancelApiRequest);

            if (productBuyCancelApiResponse.totalPrice() > 0) {
                PointUseCancelApiRequest pointUseCancelApiRequest = new PointUseCancelApiRequest(command.orderId().toString());
                pointApiClient.cancel(pointUseCancelApiRequest);
            }

            orderService.fail(command.orderId());
        }
    }
}
