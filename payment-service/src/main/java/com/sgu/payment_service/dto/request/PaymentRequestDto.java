package com.sgu.payment_service.dto.request;

import com.sgu.payment_service.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {
    private UUID userId;
    private BigDecimal amount;
    private PaymentType type;  // DEPOSIT hoặc WITHDRAW
}