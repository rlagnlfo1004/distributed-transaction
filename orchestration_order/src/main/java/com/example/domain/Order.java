package com.example.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Order() {
        status = OrderStatus.CREATED;
    }



    public enum OrderStatus {
        CREATED, COMPLETED, REQUESTED, FAILED;
    }
    public void complete() {
        status = OrderStatus.COMPLETED;
    }

    public void request() {
        if (this.status != OrderStatus.CREATED) {
            throw new RuntimeException("잘못된 요청입니다.");
        }

        status = OrderStatus.REQUESTED;
    }

    public void fail() {
        if (this.status != OrderStatus.REQUESTED) {
            throw new RuntimeException("잘못된 요청입니다.");
        }

        status = OrderStatus.FAILED;
    }
}
