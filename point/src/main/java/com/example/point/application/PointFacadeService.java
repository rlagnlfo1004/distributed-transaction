package com.example.point.application;

import com.example.point.application.dto.PointReserveCancelCommand;
import com.example.point.application.dto.PointReserveCommand;
import com.example.point.application.dto.PointReserveConfirmCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PointFacadeService {

    private final PointService pointService;

    public void tryReserve(PointReserveCommand command) {
        int tryCount = 0;

        while (tryCount < 3) {
            try {
                pointService.tryReserve(command);
                return;
            } catch (OptimisticLockingFailureException e) {
                log.info("[PointFacadeService] Optimistic Lock 재시도");
                tryCount++;
            }
        }

        throw new RuntimeException("예약에 실패하였습니다.");
    }

    public void confirmReserve(PointReserveConfirmCommand command) {
        int tryCount = 0;

        while (tryCount < 3) {
            try {
                pointService.confirmReserve(command);
                return;
            } catch (OptimisticLockingFailureException e) {
                log.info("[PointFacadeService] Optimistic Lock 재시도");
                tryCount++;
            }
        }

        throw new RuntimeException("예약에 실패하였습니다.");
    }

    public void cancelReserve(PointReserveCancelCommand command) {
        int tryCount = 0;

        while (tryCount < 3) {
            try {
                pointService.cancelReserve(command);
                return;
            } catch (OptimisticLockingFailureException e) {
                log.info("[PointFacadeService] Optimistic Lock 재시도");
                tryCount++;
            }
        }

        throw new RuntimeException("예약에 실패하였습니다.");
    }
}


