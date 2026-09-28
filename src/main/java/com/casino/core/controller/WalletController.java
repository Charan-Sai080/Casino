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

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.data.redis.core.StringRedisTemplate;
import java.util.concurrent.TimeUnit;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;
    private final StringRedisTemplate redisTemplate;

    public WalletController(WalletService walletService, StringRedisTemplate redisTemplate) {
        this.walletService = walletService;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/transactions")
    public ResponseEntity<BigDecimal> processTransaction(
            @RequestBody TransactionRequest request, 
            @RequestHeader(value = "Idempotency-Key", required = false) String headerIdempotencyKey,
            @SessionAttribute(name = "account_id", required = false) String accountIdStr) {
        
        if (accountIdStr == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String idempotencyKey = headerIdempotencyKey != null ? headerIdempotencyKey : request.getIdempotencyKey();

        if (idempotencyKey != null) {
            String cached = redisTemplate.opsForValue().get("idempotency:" + idempotencyKey);
            if (cached != null) {
                return ResponseEntity.ok(new BigDecimal(cached));
            }
        }
        
        UUID accountId = UUID.fromString(accountIdStr);

        BigDecimal newBalance = walletService.processTransaction(
                accountId,
                request.getAmount(),
                request.getType(),
                idempotencyKey
        );

        if (idempotencyKey != null) {
            redisTemplate.opsForValue().set("idempotency:" + idempotencyKey, newBalance.toString(), 24, TimeUnit.HOURS);
        }

        return ResponseEntity.ok(newBalance);
    }
}
