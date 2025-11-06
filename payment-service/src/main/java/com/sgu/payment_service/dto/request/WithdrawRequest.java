package com.sgu.payment_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawRequest {
    private String userId;
    private BigDecimal amount;
    private String bankAccount;
    private String bankName;
    private String description;
}