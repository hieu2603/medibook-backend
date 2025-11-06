package com.sgu.payment_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositRequest {
    private String userId;
    private BigDecimal amount;
    private String paymentMethod; // MOMO, VNPAY, STRIPE, etc.
    private String description;
}