package com.sgu.notification_service.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {
    APPOINTMENT_SUCCESS("Đặt lịch khám thành công"),
    APPOINTMENT_CANCELED("Lịch khám đã bị hủy");

    private final String message;
}
