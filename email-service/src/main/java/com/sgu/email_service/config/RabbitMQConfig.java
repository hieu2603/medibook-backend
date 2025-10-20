package com.sgu.email_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EMAIL_EXCHANGE = "email.exchange";

    // Welcome Email
    public static final String EMAIL_WELCOME_QUEUE = "email.welcome.queue";
    public static final String EMAIL_WELCOME_ROUTING_KEY = "EMAIL.WELCOME";

    // Reset Password Email
    public static final String EMAIL_RESET_PASSWORD_QUEUE = "email.reset-password.queue";
    public static final String EMAIL_RESET_PASSWORD_ROUTING_KEY = "EMAIL.RESET_PASSWORD";

    // Booking Success Email
    public static final String EMAIL_BOOKING_SUCCESS_QUEUE = "email.booking-success.queue";
    public static final String EMAIL_BOOKING_SUCCESS_ROUTING_KEY = "EMAIL.BOOKING_SUCCESS";

    // Booking Canceled Email
    public static final String EMAIL_BOOKING_CANCELED_QUEUE = "email.booking-canceled.queue";
    public static final String EMAIL_BOOKING_CANCELED_ROUTING_KEY = "EMAIL.BOOKING_CANCELED";

    @Bean
    public TopicExchange emailExchange() {
        return new TopicExchange(EMAIL_EXCHANGE);
    }

    // Welcome Queue + Binding
    @Bean
    public Queue welcomeQueue() {
        return new Queue(EMAIL_WELCOME_QUEUE, true);
    }

    @Bean
    public Binding welcomeBinding(Queue welcomeQueue, TopicExchange emailExchange) {
        return BindingBuilder
                .bind(welcomeQueue)
                .to(emailExchange)
                .with(EMAIL_WELCOME_ROUTING_KEY);
    }

    // Reset Password Queue + Binding
    @Bean
    public Queue resetPasswordQueue() {
        return new Queue(EMAIL_RESET_PASSWORD_QUEUE, true);
    }

    @Bean
    public Binding resetPasswordBinding(Queue resetPasswordQueue, TopicExchange emailExchange) {
        return BindingBuilder
                .bind(resetPasswordQueue)
                .to(emailExchange)
                .with(EMAIL_RESET_PASSWORD_ROUTING_KEY);
    }

    // Booking Success Queue + Binding
    @Bean
    public Queue bookingSuccessQueue() {
        return new Queue(EMAIL_BOOKING_SUCCESS_QUEUE, true);
    }

    @Bean
    public Binding bookingSuccessBinding(Queue bookingSuccessQueue, TopicExchange emailExchange) {
        return BindingBuilder
                .bind(bookingSuccessQueue)
                .to(emailExchange)
                .with(EMAIL_BOOKING_SUCCESS_ROUTING_KEY);
    }

    // Booking Canceled Queue + Binding
    @Bean
    public Queue bookingCanceledQueue() {
        return new Queue(EMAIL_BOOKING_CANCELED_QUEUE, true);
    }

    @Bean
    public Binding bookingCanceledBinding(Queue bookingCanceledQueue, TopicExchange emailExchange) {
        return BindingBuilder
                .bind(bookingCanceledQueue)
                .to(emailExchange)
                .with(EMAIL_BOOKING_CANCELED_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter(new ObjectMapper());
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
