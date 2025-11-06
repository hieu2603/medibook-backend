package com.sgu.user_service.dto.request;

import com.sgu.user_service.constant.PaymentType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class PaymentRequestDto {
    private UUID userId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1", message = "Amount must be greater than 0")
    private BigDecimal amount;

    private PaymentType type;
}
