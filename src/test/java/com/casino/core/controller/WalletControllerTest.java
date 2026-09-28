package com.casino.core.controller;

import com.casino.core.dto.TransactionRequest;
import com.casino.core.service.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WalletController.class)
public class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    @MockBean
    private StringRedisTemplate redisTemplate;

    @MockBean
    private ValueOperations<String, String> valueOperations;

    @Test
    public void testProcessTransactionValidRequest() throws Exception {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.setIfAbsent(anyString(), anyString(), any())).thenReturn(true);
        when(walletService.processTransaction(any(UUID.class), any(BigDecimal.class), any(), any()))
                .thenReturn(new BigDecimal("100.00"));

        String requestBody = "{\"amount\": 50.0, \"type\": \"DEPOSIT\", \"idempotencyKey\": \"test-key\"}";

        mockMvc.perform(post("/api/wallet/transactions")
                .sessionAttr("account_id", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    public void testProcessTransactionInvalidRequest() throws Exception {
        String requestBody = "{\"amount\": -50.0, \"type\": \"DEPOSIT\"}";

        mockMvc.perform(post("/api/wallet/transactions")
                .sessionAttr("account_id", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}
