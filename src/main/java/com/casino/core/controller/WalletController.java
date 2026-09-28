package com.casino.core.controller;

import com.casino.core.dto.TransactionRequest;
import com.casino.core.service.WalletService;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/transactions")
    public ResponseEntity<BigDecimal> processTransaction(
            @RequestBody TransactionRequest request, 
            @SessionAttribute(name = "account_id", required = false) String accountIdStr) {
        
        if (accountIdStr == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        UUID accountId = UUID.fromString(accountIdStr);

        BigDecimal newBalance = walletService.processTransaction(
                accountId,
                request.getAmount(),
                request.getType(),
                request.getIdempotencyKey()
        );

        return ResponseEntity.ok(newBalance);
    }
}
