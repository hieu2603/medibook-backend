package com.sgu.appointment_service.dto.request.transfer;

import com.sgu.appointment_service.constant.TransferType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class TransferRequestDto {
    private UUID fromUserId;
    private UUID toUserId;
    private BigDecimal amount;
    private TransferType type;
}
