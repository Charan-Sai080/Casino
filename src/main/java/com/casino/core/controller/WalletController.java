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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private static final Logger logger = LoggerFactory.getLogger(WalletController.class);
    private final WalletService walletService;
    private final StringRedisTemplate redisTemplate;

    public WalletController(WalletService walletService, StringRedisTemplate redisTemplate) {
        this.walletService = walletService;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/transactions")
    public ResponseEntity<BigDecimal> processTransaction(
            @jakarta.validation.Valid @RequestBody TransactionRequest request, 
            @RequestHeader(value = "Idempotency-Key", required = false) String headerIdempotencyKey,
            @SessionAttribute(name = "account_id", required = false) String accountIdStr) {
        
        if (accountIdStr == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String idempotencyKey = headerIdempotencyKey != null ? headerIdempotencyKey : request.getIdempotencyKey();

        if (idempotencyKey != null) {
            try {
                String cached = redisTemplate.opsForValue().get("idempotency:" + idempotencyKey);
                if (cached != null) {
                    try {
                        return ResponseEntity.ok(new BigDecimal(cached));
                    } catch (NumberFormatException e) {
                        logger.warn("Invalid cached value for idempotency key: {}", idempotencyKey, e);
                    }
                }
                
                Boolean isNew = redisTemplate.opsForValue().setIfAbsent("lock:" + idempotencyKey, "PENDING", java.time.Duration.ofMinutes(1));
                if (Boolean.FALSE.equals(isNew)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).build();
                }
            } catch (Exception e) {
                logger.warn("Redis operations failed for idempotency key: {}", idempotencyKey, e);
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
            try {
                redisTemplate.opsForValue().set("idempotency:" + idempotencyKey, newBalance.toString(), 24, TimeUnit.HOURS);
            } catch (Exception e) {
                logger.warn("Redis set failed for idempotency key: {}", idempotencyKey, e);
            }
        }

        return ResponseEntity.ok(newBalance);
    }
}
