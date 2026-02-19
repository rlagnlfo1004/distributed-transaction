package com.example.controller;

import com.example.application.ProductService;
import com.example.application.RedisLockService;
import com.example.application.dto.ProductBuyResult;
import com.example.controller.dto.ProductBuyRequest;
import com.example.controller.dto.ProductBuyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final RedisLockService redisLockService;

    @PostMapping("/product/buy")
    public ProductBuyResponse buy(@RequestBody ProductBuyRequest request) {
        String key = "product:orchestration" + request.requestId();
        boolean acquiredLock = redisLockService.tryLock(key, request.requestId());

        if (!acquiredLock) {
            System.out.println("락 획득에 실패하였습니다.");
            throw new RuntimeException("락 획득에 실패하였습니다.");
        }

        try {
            ProductBuyResult buyResult = productService.buy(request.toCommand());
            return new ProductBuyResponse(buyResult.totalPrice());
        } finally {
            redisLockService.releaseLock(key);
        }
    }
}
