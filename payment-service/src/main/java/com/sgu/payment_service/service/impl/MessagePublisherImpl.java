package com.sgu.payment_service.service.impl;

import com.sgu.payment_service.config.RabbitMQConfig;
import com.sgu.payment_service.dto.message.BalanceUpdateMessage;
import com.sgu.payment_service.dto.message.NotificationMessage;
import com.sgu.payment_service.service.MessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessagePublisherImpl implements MessagePublisher {
    
    private final RabbitTemplate rabbitTemplate;
    
    @Override
    public void publishBalanceUpdate(BalanceUpdateMessage message) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.PAYMENT_EXCHANGE,
                    RabbitMQConfig.BALANCE_UPDATE_ROUTING_KEY,
                    message
            );
            log.info("Balance update message sent: {}", message);
        } catch (Exception e) {
            log.error("Error publishing balance update message", e);
            throw new RuntimeException("Failed to publish balance update message", e);
        }
    }
    
    @Override
    public void publishNotification(NotificationMessage message) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.PAYMENT_EXCHANGE,
                    RabbitMQConfig.NOTIFICATION_ROUTING_KEY,
                    message
            );
            log.info("Notification message sent: {}", message);
        } catch (Exception e) {
            log.error("Error publishing notification message", e);
            throw new RuntimeException("Failed to publish notification message", e);
        }
    }
}