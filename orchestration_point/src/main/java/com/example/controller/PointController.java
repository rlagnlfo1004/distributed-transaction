package com.example.controller;

import com.example.application.PointService;
import com.example.application.RedisLockService;
import com.example.controller.dto.PointUseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;
    private final RedisLockService redisLockService;

    @PostMapping("/point/use")
    public void use(@RequestBody PointUseRequest request) {
        String key = "point:orchestration" + request.requestId();
        boolean acquiredLock = redisLockService.tryLock(key, request.requestId());

        if (!acquiredLock) {
            System.out.println("락 획득에 실패하였습니다.");
            throw new RuntimeException("락 획득에 실패하였습니다.");
        }

        try {
            pointService.use(request.toCommand());
        } finally {
            redisLockService.releaseLock(key);
        }
    }
}
