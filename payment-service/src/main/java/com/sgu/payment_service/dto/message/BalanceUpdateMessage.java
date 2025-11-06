package com.sgu.payment_service.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceUpdateMessage {
    private String userId;
    private BigDecimal amount;
    private String operation; // INCREASE or DECREASE
}