package com.example.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private OrderStatus status;

    public Order() {
        status = OrderStatus.CREATED;
    }

    public enum OrderStatus {
        CREATED, COMPLETED
    }

    public void complete() {
        status = OrderStatus.COMPLETED;
    }
}
