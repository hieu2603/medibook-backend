package com.sgu.appointment_service.dto.request;

import com.sgu.appointment_service.enums.TransferType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class TransferRequestDto {
    @NotNull(message = "Sender ID is required")
    private UUID fromUserId;

    @NotNull(message = "Receiver ID is required")
    private UUID toUserId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Transfer type is required")
    private TransferType type;
}
