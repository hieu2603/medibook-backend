package com.sgu.notification_service.dto;
import com.sgu.notification_service.constant.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage {
    private UUID notificationId;
    private UUID userId;
    private String title;
    private String message;
    private boolean isRead;
    private LocalDateTime createdAt;
    private NotificationType type;
}

