package com.sgu.payment_service.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage {
    private String userId;
    private String title;
    private String message;
    private String type; // DEPOSIT, WITHDRAW
}