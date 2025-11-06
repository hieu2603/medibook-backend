package com.sgu.payment_service.service;

import com.sgu.payment_service.dto.message.BalanceUpdateMessage;
import com.sgu.payment_service.dto.message.NotificationMessage;

public interface MessagePublisher {
    void publishBalanceUpdate(BalanceUpdateMessage message);
    void publishNotification(NotificationMessage message);
}