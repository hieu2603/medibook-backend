package com.sgu.auth_service.event;

import com.sgu.auth_service.config.RabbitMQConfig;
import com.sgu.auth_service.dto.request.email.EmailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmailEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendWelcomeEmail(String to) {
        Map<String, Object> variables = Map.of(
                "email", to
        );

        EmailMessage emailMessage = EmailMessage.builder()
                .to(to)
                .variables(variables)
                .build();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EMAIL_EXCHANGE,
                RabbitMQConfig.EMAIL_WELCOME_ROUTING_KEY,
                emailMessage
        );
    }

    public void sendForgotPasswordEmail(String to, String resetLink) {
        Map<String, Object> variables = Map.of(
                "email", to,
                "resetLink", resetLink
        );

        EmailMessage emailMessage = EmailMessage.builder()
                .to(to)
                .variables(variables)
                .build();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EMAIL_EXCHANGE,
                RabbitMQConfig.EMAIL_FORGOT_PASSWORD_ROUTING_KEY,
                emailMessage
        );
    }
}
