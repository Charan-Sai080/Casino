package com.casino.core.controller;

import com.casino.core.domain.Account;
import com.casino.core.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
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

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("account_id") != null) {
            return ResponseEntity.ok(Map.of("message", "Session already exists"));
        }

        Account account = sessionService.createSessionAccount();
        
        if (session != null) {
            request.changeSessionId();
        }
        session = request.getSession(true);
        session.setAttribute("account_id", account.getId().toString());

        Map<String, String> response = new HashMap<>();
        response.put("account_id", account.getId().toString());
        response.put("message", "Session created successfully");

        return ResponseEntity.ok(response);
    }
}
