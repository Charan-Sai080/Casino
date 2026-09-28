package com.casino.core.repository;

import com.casino.core.domain.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, UUID> {
    @Query("SELECT COALESCE(SUM(t.amount), 0.0) FROM WalletTransaction t WHERE t.account.id = :accountId")
    BigDecimal getBalanceForAccount(@Param("accountId") UUID accountId);
}
