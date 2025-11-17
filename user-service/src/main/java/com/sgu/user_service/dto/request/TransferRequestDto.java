package com.sgu.user_service.dto.request;

import com.sgu.user_service.constant.TransferType;
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
    private UUID fromUserId; // Thường là user id của patient

    @NotNull(message = "Receiver ID is required")
    private UUID toUserId; // Thường là user id của clinic

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Transfer type is required")
    private TransferType type;
}
