package com.sgu.notification_service.controller;

import com.sgu.notification_service.config.RabbitMQConfig;
import com.sgu.notification_service.dto.NotificationMessage;
import com.sgu.notification_service.dto.request.NotificationRequest;
import com.sgu.notification_service.model.Notification;
import com.sgu.notification_service.service.NotificationService;
import com.sgu.notification_service.constant.NotificationType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;
    private final RabbitTemplate rabbitTemplate;

    // API tạo thông báo (và gửi realtime)

    @PostMapping("/create")
    public ResponseEntity<String> createNotification(@RequestBody NotificationRequest request) {
        NotificationMessage message = new NotificationMessage();
        message.setUserId(request.getUserId());
        message.setTitle(request.getTitle());
        message.setMessage(request.getMessage());
        message.setType(NotificationType.valueOf(request.getType()));

        // Gửi vào queue, NotificationService listener sẽ xử lý
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.NOTIFICATION_EXCHANGE,
                RabbitMQConfig.NOTIFICATION_APPOINTMENT_SUCCESS_ROUTING_KEY,
                message);

        return ResponseEntity.ok("Notification message sent to RabbitMQ");
    }

    // Lấy danh sách thông báo của user

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable UUID userId) {
        List<Notification> notifications = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    // Đánh dấu 1 thông báo là đã đọc

    @PutMapping("/{notificationId}/mark-read")
    public ResponseEntity<String> markAsRead(@PathVariable UUID notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok("Notification marked as read");
    }

    // Đánh dấu tất cả thông báo của user là đã đọc

    @PutMapping("/user/{userId}/mark-all-read")
    public ResponseEntity<String> markAllAsRead(@PathVariable UUID userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok("All notifications marked as read");
    }

    // Xóa thông báo

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<String> deleteNotification(@PathVariable UUID notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok("Notification deleted");
    }

    @PostMapping("/test/send")
    public ResponseEntity<String> sendTestNotification(@RequestParam UUID userId, @RequestParam String messageText) {
        NotificationMessage message = new NotificationMessage();
        message.setUserId(userId);
        message.setMessage(messageText);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.NOTIFICATION_EXCHANGE,
                RabbitMQConfig.NOTIFICATION_APPOINTMENT_SUCCESS_ROUTING_KEY,
                message);

        return ResponseEntity.ok("Test message sent to RabbitMQ");
    }

    @DeleteMapping("/user/{userId}/delete-all")
    public ResponseEntity<String> deleteAllNotifications(@PathVariable UUID userId) {
        notificationService.deleteAllNotifications(userId);
        return ResponseEntity.ok("All notifications deleted for user " + userId);
    }

}
