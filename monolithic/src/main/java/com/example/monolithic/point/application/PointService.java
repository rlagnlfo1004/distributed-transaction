package com.example.monolithic.point.application;

import com.example.monolithic.point.domain.Point;
import com.example.monolithic.point.infrastructure.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;

    @Transactional
    public void use(Long userId, Long amount) {
        Point point = pointRepository.findByUserId(userId);

        if (point == null) {
            throw new RuntimeException("포인트가 존재하지 않습니다.");
        }

        point.use(amount);
        pointRepository.save(point);
    }
}
