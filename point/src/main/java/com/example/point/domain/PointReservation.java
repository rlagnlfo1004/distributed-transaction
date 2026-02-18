package com.example.point.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "point_reservations")
public class PointReservation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String requestId;

    private Long pointId;

    private Long reservedAmount;

    @Enumerated(EnumType.STRING)
    private PointReservationStatus status;

    public PointReservation() {}

    public PointReservation(String requestId, Long pointId, Long reservedAmount) {
        this.requestId = requestId;
        this.pointId = pointId;
        this.reservedAmount = reservedAmount;
        status = PointReservationStatus.RESERVED;
    }

    public enum PointReservationStatus {
        RESERVED,
        CONFIRMED,
        CANCELLED
    }

    public void confirm() {
        if (status == PointReservationStatus.CANCELLED) {
            throw new RuntimeException("취소된 예약은 확정할 수 없습니다.");
        }

        this.status = PointReservationStatus.CONFIRMED;
    }

    public void cancel() {
        if (status == PointReservationStatus.CONFIRMED) {
            throw new RuntimeException("확정된 예약은 취소할 수 없습니다.");
        }

        this.status = PointReservationStatus.CANCELLED;
    }
}
