package com.casino.core.controller;

import com.casino.core.domain.Account;
import com.casino.core.repository.AccountRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final AccountRepository accountRepository;

    public SessionController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createSession(HttpSession session) {
        Account account = new Account();
        account = accountRepository.save(account);

        session.setAttribute("account_id", account.getId().toString());

        Map<String, String> response = new HashMap<>();
        response.put("account_id", account.getId().toString());
        response.put("message", "Session created successfully");

        return ResponseEntity.ok(response);
    }
}
