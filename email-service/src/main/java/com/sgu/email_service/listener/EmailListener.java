package com.sgu.email_service.listener;

import com.sgu.email_service.config.RabbitMQConfig;
import com.sgu.email_service.constant.EmailType;
import com.sgu.email_service.dto.EmailMessage;
import com.sgu.email_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailListener {
    private final EmailService emailService;

    @RabbitListener(queues = RabbitMQConfig.EMAIL_WELCOME_PATIENT_QUEUE)
    public void handleWelcomePatientEmail(EmailMessage emailMessage) {
        log.info("Receive EMAIL.WELCOME_PATIENT message for: {}", emailMessage.getTo());
        emailService.sendEmail(emailMessage, EmailType.WELCOME_PATIENT);
    }

    @RabbitListener(queues = RabbitMQConfig.EMAIL_WELCOME_CLINIC_QUEUE)
    public void handleWelcomeClinicEmail(EmailMessage emailMessage) {
        log.info("Receive EMAIL.WELCOME_CLINIC message for: {}", emailMessage.getTo());
        emailService.sendEmail(emailMessage, EmailType.WELCOME_CLINIC);
    }

    @RabbitListener(queues = RabbitMQConfig.EMAIL_FORGOT_PASSWORD_QUEUE)
    public void handleResetPasswordEmail(EmailMessage emailMessage) {
        log.info("Receive EMAIL.RESET_PASSWORD message for: {}", emailMessage.getTo());
        emailService.sendEmail(emailMessage, EmailType.FORGOT_PASSWORD);
    }

    @RabbitListener(queues = RabbitMQConfig.EMAIL_BOOKING_SUCCESS_QUEUE)
    public void handleBookingSuccessEmail(EmailMessage emailMessage) {
        log.info("Receive EMAIL.BOOKING_SUCCESS message for: {}", emailMessage.getTo());
        emailService.sendEmail(emailMessage, EmailType.BOOKING_SUCCESS);
    }

    @RabbitListener(queues = RabbitMQConfig.EMAIL_BOOKING_CANCELED_QUEUE)
    public void handleBookingCanceledEmail(EmailMessage emailMessage) {
        log.info("Receive EMAIL.BOOKING_CANCELED message for: {}", emailMessage.getTo());
        emailService.sendEmail(emailMessage, EmailType.BOOKING_CANCELED);
    }
}
