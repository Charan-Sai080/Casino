package com.casino.core.service;

import com.casino.core.domain.Account;
import com.casino.core.domain.WalletTransaction;
import com.casino.core.exception.InsufficientFundsException;
import com.casino.core.repository.AccountRepository;
import com.casino.core.repository.WalletTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletService {

    private final AccountRepository accountRepository;
    private final WalletTransactionRepository walletTransactionRepository;

    public WalletService(AccountRepository accountRepository, WalletTransactionRepository walletTransactionRepository) {
        this.accountRepository = accountRepository;
        this.walletTransactionRepository = walletTransactionRepository;
    }

    @Transactional
    public BigDecimal processTransaction(UUID accountId, BigDecimal amount, String type, String idempotencyKey) {
        Account account = accountRepository.findByIdForUpdate(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        BigDecimal currentBalance = walletTransactionRepository.getBalanceForAccount(accountId);
        if (amount.compareTo(BigDecimal.ZERO) < 0 && currentBalance.add(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        WalletTransaction transaction = new WalletTransaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setTransactionType(type);
        transaction.setIdempotencyKey(idempotencyKey);

        walletTransactionRepository.save(transaction);

        return currentBalance.add(amount);
    }
}
