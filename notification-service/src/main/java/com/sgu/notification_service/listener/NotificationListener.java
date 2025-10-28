// package com.sgu.notification_service.listener;

// import com.sgu.notification_service.config.RabbitMQConfig;
// import com.sgu.notification_service.constant.NotificationType;
// import com.sgu.notification_service.dto.NotificationMessage;
// import com.sgu.notification_service.service.NotificationService;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.amqp.rabbit.annotation.RabbitListener;
// import org.springframework.stereotype.Component;

// @Component
// @RequiredArgsConstructor
// @Slf4j
// public class NotificationListener {

//     private final NotificationService notificationService;

//     /**
//      * Khi nhận thông báo đặt lịch thành công
//      */
//     @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_APPOINTMENT_SUCCESS_QUEUE)
//     public void handleAppointmentSuccessNotification(NotificationMessage message) {
//         log.info("📩 Received [APPOINTMENT_SUCCESS] for user: {}", message.getUserId());
//         message.setType(NotificationType.APPOINTMENT_SUCCESS);
//         notificationService.sendNotification(message);
//     }

//     /**
//      * Khi nhận thông báo hủy lịch
//      */
//     @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_APPOINTMENT_CANCELED_QUEUE)
//     public void handleAppointmentCanceledNotification(NotificationMessage message) {
//         log.info("📩 Received [APPOINTMENT_CANCELED] for user: {}", message.getUserId());
//         message.setType(NotificationType.APPOINTMENT_CANCELED);
//         notificationService.sendNotification(message);
//     }
// }
package com.sgu.notification_service.listener;

import com.sgu.notification_service.config.RabbitMQConfig;
import com.sgu.notification_service.constant.NotificationType;
import com.sgu.notification_service.dto.NotificationMessage;
import com.sgu.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationListener {

    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_APPOINTMENT_SUCCESS_QUEUE)
    public void handleAppointmentSuccess(NotificationMessage message) {
        NotificationType type = message.getType();
        if (message.getMessage() == null) {
            message.setMessage(type.getMessage());
        }

        notificationService.sendNotification(message);
    }

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_APPOINTMENT_CANCELED_QUEUE)
    public void handleAppointmentCanceled(NotificationMessage message) {
        NotificationType type = message.getType();

        if (message.getMessage() == null) {
            message.setMessage(type.getMessage());
        }

        notificationService.sendNotification(message);
    }
}
