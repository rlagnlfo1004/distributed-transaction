package com.example.product.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(
    name = "product_reservations",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_request_product",
        columnNames = {"requestId", "productId"}
    )
)
public class ProductReservation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String requestId; // 멱등성 설계를 위한 필드

    private Long productId;

    private Long reservedQuantity;

    private Long reservedPrice;

    @Enumerated(EnumType.STRING)
    private ProductReservationStatus status;

    public ProductReservation() {}

    public ProductReservation(String requestId, Long productId, Long reservedQuantity, Long reservedPrice) {
        this.requestId = requestId;
        this.productId = productId;
        this.reservedQuantity = reservedQuantity;
        this.reservedPrice = reservedPrice;
        status = ProductReservationStatus.RESERVED;
    }

    public enum ProductReservationStatus {
        RESERVED,
        CONFIRMED,
        CANCELED
    }
}
