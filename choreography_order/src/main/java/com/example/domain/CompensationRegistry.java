package com.example.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Table(name = "compensation_registries")
@Entity
@Getter
public class CompensationRegistry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    @Enumerated(EnumType.STRING)
    private CompensationRegistryStatus status;

    public enum CompensationRegistryStatus {
        PENDING, COMPLETE
    }

    public CompensationRegistry() {}

    public CompensationRegistry(Long orderId) {
        this.orderId = orderId;
        this.status = CompensationRegistryStatus.PENDING;
    }
}
