package com.sgu.email_service.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailType {
    WELCOME("Chào mừng bạn đến với Medibook!", "welcome"),
    RESET_PASSWORD("Yêu cầu đặt lại mật khẩu Medibook của bạn", "reset-password"),
    BOOKING_SUCCESS("Đặt lịch khám thành công", "booking-success"),
    BOOKING_CANCELED("Lịch khám của bạn đã bị hủy", "booking-canceled");

    private final String subject;
    private final String template;
}
