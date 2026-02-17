package com.example.point.application;

import com.example.point.application.dto.PointReserveCommand;
import com.example.point.domain.Point;
import com.example.point.domain.PointReservation;
import com.example.point.infrastructure.PointRepository;
import com.example.point.infrastructure.PointReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;
    private final PointReservationRepository pointReservationRepository;

    public void tryReserve(PointReserveCommand command) {
        PointReservation reservation = pointReservationRepository.findByRequestId(command.requestId());

        if (reservation != null) {
            log.info("이미 예약된 요청입니다.");
            return;
        }

        Point point = pointRepository.findByUserId(command.userId());

        point.reserve(command.reserveAmount());
        pointReservationRepository.save(new PointReservation(
                command.requestId(), point.getId(), command.reserveAmount()
        ));
    }
}
