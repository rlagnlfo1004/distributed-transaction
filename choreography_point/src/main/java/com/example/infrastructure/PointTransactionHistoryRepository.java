package com.example.infrastructure;

import com.example.domain.PointTransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointTransactionHistoryRepository extends JpaRepository<PointTransactionHistory, Long> {
    PointTransactionHistory findByRequestIdAndTransactionType(
            String requestId,
            PointTransactionHistory.TransactionType transactionType
    );
}
