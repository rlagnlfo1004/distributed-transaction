package com.example.order.infrastructure.product;

import com.example.order.infrastructure.product.dto.ProductReserveApiRequest;
import com.example.order.infrastructure.product.dto.ProductReserveApiResponse;
import com.example.order.infrastructure.product.dto.ProductReserveCancelApiRequest;
import com.example.order.infrastructure.product.dto.ProductReserveConfirmApiRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
public class ProductApiClient {

    private final RestClient restClient;

    public ProductReserveApiResponse reserveProduct(ProductReserveApiRequest request) {
        return restClient.post()
                .uri("/product/reserve")
                .body(request)
                .retrieve()
                .body(ProductReserveApiResponse.class);
    }

    public void confirmProduct(ProductReserveConfirmApiRequest request) {
        restClient.post()
                .uri("/product/confirm")
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }

    public void cancelProduct(ProductReserveCancelApiRequest request) {
        restClient.post()
                .uri("/product/cancel")
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }
}
