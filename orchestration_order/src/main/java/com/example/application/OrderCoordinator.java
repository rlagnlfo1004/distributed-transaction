package com.example.application;

import com.example.application.dto.OrderDto;
import com.example.application.dto.PlaceOrderCommand;
import com.example.domain.CompensationRegistry;
import com.example.infrastructure.CompensationRegistryRepository;
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
    private final CompensationRegistryRepository compensationRegistryRepository;

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
            rollback(command.orderId());

            throw e;
        }
    }

    private void rollback(Long orderId) {
        try {
            ProductBuyCancelApiRequest productBuyCancelApiRequest = new ProductBuyCancelApiRequest(orderId.toString());
            ProductBuyCancelApiResponse productBuyCancelApiResponse = productApiClient.cancel(productBuyCancelApiRequest);

            if (productBuyCancelApiResponse.totalPrice() > 0) {
                PointUseCancelApiRequest pointUseCancelApiRequest = new PointUseCancelApiRequest(orderId.toString());
                pointApiClient.cancel(pointUseCancelApiRequest);
            }

            orderService.fail(orderId);
        } catch (Exception e) {
            compensationRegistryRepository.save(new CompensationRegistry(orderId));
            throw e;
        }
    }
}
