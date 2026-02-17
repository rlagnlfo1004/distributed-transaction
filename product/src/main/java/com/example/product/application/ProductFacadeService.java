package com.example.product.application;

import com.example.product.application.dto.ProductReserveCommand;
import com.example.product.application.dto.ProductReserveResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductFacadeService {

    private final ProductService productService;

    public ProductReserveResult tryReserve(ProductReserveCommand command) {
        int tryCount = 0;

        while (tryCount < 3) {
            try {
                return productService.tryReserve(command);
            } catch(OptimisticLockingFailureException e) {
                log.info("[ProductFacadeService] Optimistic Lock 재시도");
                tryCount++;
            }
        }

        throw new RuntimeException("예약에 실패하였습니다.");
    }
}
