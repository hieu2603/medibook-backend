package com.sgu.notification_service.service;

import com.sgu.notification_service.dto.NotificationMessage;
import com.sgu.notification_service.model.Notification;
import java.util.List;
import java.util.UUID;

public interface NotificationService {

    List<Notification> getNotificationsByUserId(UUID userId);

    Notification createNotification(UUID userId, String title, String message);

    void sendNotification(NotificationMessage message);

    void markAsRead(UUID notificationId);

    void markAllAsRead(UUID userId);

    void deleteNotification(UUID notificationId);

    void deleteAllNotifications(UUID userId);

}
