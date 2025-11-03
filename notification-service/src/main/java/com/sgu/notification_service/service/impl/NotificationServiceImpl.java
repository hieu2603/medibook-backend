package com.sgu.notification_service.service.impl;

import com.sgu.notification_service.dto.NotificationMessage;
import com.sgu.notification_service.model.Notification;
import com.sgu.notification_service.repository.NotificationRepository;
import com.sgu.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public List<Notification> getNotificationsByUserId(UUID userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    // @Override
    // public Notification createNotification(UUID userId, String title, String
    // message) {
    // Notification notification = Notification.builder()
    // .userId(userId)
    // .title(title)
    // .message(message)
    // .isRead(false)
    // .createdAt(LocalDateTime.now())
    // .build();

    // Notification saved = notificationRepository.save(notification);

    // messagingTemplate.convertAndSend("/topic/notifications/" + userId, saved);
    // log.info(" Sent notification to user {}: {}", userId, message);
    // return saved;
    // }

    // @Override
    // public void sendNotification(NotificationMessage message) {
    // UUID userId = message.getUserId();

    // Notification notification = Notification.builder()
    // .userId(userId)
    // .title(message.getTitle() != null ? message.getTitle() : "Test Notification")
    // .message(message.getMessage())
    // .isRead(false)
    // .createdAt(LocalDateTime.now())
    // .build();

    // notificationRepository.save(notification);

    // messagingTemplate.convertAndSend("/topic/notifications/" + userId,
    // notification);

    // log.info("[RabbitMQ] Sent notification to user {}: {}", userId,
    // notification.getTitle());
    // }

    @Override
    public Notification createNotification(UUID userId, String title, String message) {
        Notification notification = Notification.builder()
                .userId(userId)
                .title(title)
                .message(message)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        Notification saved = notificationRepository.save(notification);

        // Chuyển sang NotificationMessage trước khi gửi WebSocket
        NotificationMessage msgToSend = NotificationMessage.builder()
                .notificationId(saved.getNotificationId())
                .userId(saved.getUserId())
                .title(saved.getTitle())
                .message(saved.getMessage())
                .isRead(saved.isRead())
                .createdAt(saved.getCreatedAt())
                .type(saved.getType())
                .build();

        messagingTemplate.convertAndSend("/topic/notifications/" + userId, msgToSend);

        log.info("📢 Sent notification to user {}: {}", userId, message);
        return saved;
    }

    @Override
    public void sendNotification(NotificationMessage message) {
        UUID userId = message.getUserId();

        Notification notification = Notification.builder()
                .userId(userId)
                .title(message.getTitle() != null ? message.getTitle() : "Test Notification")
                .message(message.getMessage())
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .type(message.getType())
                .build();

        Notification saved = notificationRepository.save(notification);

        // Luôn gửi NotificationMessage tới frontend
        NotificationMessage msgToSend = NotificationMessage.builder()
                .notificationId(saved.getNotificationId())
                .userId(saved.getUserId())
                .title(saved.getTitle())
                .message(saved.getMessage())
                .isRead(saved.isRead())
                .createdAt(saved.getCreatedAt())
                .type(saved.getType())
                .build();

        messagingTemplate.convertAndSend("/topic/notifications/" + userId, msgToSend);

        log.info("📢 [RabbitMQ] Sent notification to user {}: {}", userId, saved.getTitle());
    }

    @Override
    public void markAsRead(UUID notificationId) {
        notificationRepository.findById(notificationId).ifPresent(n -> {
            n.setRead(true);
            notificationRepository.save(n);
        });
    }

    @Override
    public void markAllAsRead(UUID userId) {
        List<Notification> list = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        list.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(list);
    }

    @Override
    public void deleteNotification(UUID notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    @Override
    public void deleteAllNotifications(UUID userId) {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        notificationRepository.deleteAll(notifications);
        log.info(" Deleted all notifications for user {}", userId);
    }

}
