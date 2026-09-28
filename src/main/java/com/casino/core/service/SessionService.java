package com.casino.core.service;

import com.casino.core.domain.Account;
import com.casino.core.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SessionService {

    private final AccountRepository accountRepository;

    public SessionService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Account createSessionAccount() {
        Account account = new Account();
        return accountRepository.save(account);
    }
}
