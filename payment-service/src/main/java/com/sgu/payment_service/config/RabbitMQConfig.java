package com.sgu.payment_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
    // Queue names
    public static final String BALANCE_UPDATE_QUEUE = "balance.update.queue";
    public static final String NOTIFICATION_QUEUE = "notification.queue";
    
    // Exchange names
    public static final String PAYMENT_EXCHANGE = "payment.exchange";
    
    // Routing keys
    public static final String BALANCE_UPDATE_ROUTING_KEY = "balance.update";
    public static final String NOTIFICATION_ROUTING_KEY = "notification";
    
    @Bean
    public Queue balanceUpdateQueue() {
        return new Queue(BALANCE_UPDATE_QUEUE, true);
    }
    
    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE, true);
    }
    
    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(PAYMENT_EXCHANGE);
    }
    
    @Bean
    public Binding balanceUpdateBinding() {
        return BindingBuilder
                .bind(balanceUpdateQueue())
                .to(paymentExchange())
                .with(BALANCE_UPDATE_ROUTING_KEY);
    }
    
    @Bean
    public Binding notificationBinding() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(paymentExchange())
                .with(NOTIFICATION_ROUTING_KEY);
    }
    
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}