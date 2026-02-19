package com.example.order.infrastructure.point;

import com.example.order.infrastructure.point.dto.PointReserveApiRequest;
import com.example.order.infrastructure.point.dto.PointReserveCancelApiRequest;
import com.example.order.infrastructure.point.dto.PointReserveConfirmApiRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
public class PointApiClient {

    private final RestClient restClient;

    public void reservePoint(PointReserveApiRequest request) {
        restClient.post()
                .uri("/point/reserve")
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }

    public void confirmPoint(PointReserveConfirmApiRequest request) {
        restClient.post()
                .uri("/point/confrim")
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }

    public void cancelPoint(PointReserveCancelApiRequest request) {
        restClient.post()
                .uri("/point/cancel")
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }
}
