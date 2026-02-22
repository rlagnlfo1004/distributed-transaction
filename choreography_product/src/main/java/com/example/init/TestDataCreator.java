package com.example.init;

import com.example.domain.Product;
import com.example.infrastructure.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestDataCreator {

    private final ProductRepository productRepository;

    @PostConstruct
    public void createTestData() {
        Product product1 = new Product(100L, 100L);
        Product product2 = new Product(100L, 200L);

        productRepository.save(product1);
        productRepository.save(product2);
    }
}
