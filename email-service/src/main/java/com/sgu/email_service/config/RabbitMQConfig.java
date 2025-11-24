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

    // Welcome Patient Email
    public static final String EMAIL_WELCOME_PATIENT_QUEUE = "email.welcome-patient.queue";
    public static final String EMAIL_WELCOME_PATIENT_ROUTING_KEY = "EMAIL.WELCOME_PATIENT";

    // Welcome Clinic Email
    public static final String EMAIL_WELCOME_CLINIC_QUEUE = "email.welcome-clinic.queue";
    public static final String EMAIL_WELCOME_CLINIC_ROUTING_KEY = "EMAIL.WELCOME_CLINIC";

    // Forgot Password Email
    public static final String EMAIL_FORGOT_PASSWORD_QUEUE = "email.forgot-password.queue";
    public static final String EMAIL_FORGOT_PASSWORD_ROUTING_KEY = "EMAIL.FORGOT_PASSWORD";

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

    // Welcome Patient Queue + Binding
    @Bean
    public Queue welcomePatientQueue() {
        return new Queue(EMAIL_WELCOME_PATIENT_QUEUE, true);
    }

    @Bean
    public Binding welcomePatientBinding(Queue welcomePatientQueue, TopicExchange emailExchange) {
        return BindingBuilder
                .bind(welcomePatientQueue)
                .to(emailExchange)
                .with(EMAIL_WELCOME_PATIENT_ROUTING_KEY);
    }

    // Welcome Clinic Queue + Binding
    @Bean
    public Queue welcomeClinicQueue() {
        return new Queue(EMAIL_WELCOME_CLINIC_QUEUE, true);
    }

    @Bean
    public Binding welcomeClinicBinding(Queue welcomeClinicQueue, TopicExchange emailExchange) {
        return BindingBuilder
                .bind(welcomeClinicQueue)
                .to(emailExchange)
                .with(EMAIL_WELCOME_CLINIC_ROUTING_KEY);
    }

    // Reset Password Queue + Binding
    @Bean
    public Queue forgotPasswordQueue() {
        return new Queue(EMAIL_FORGOT_PASSWORD_QUEUE, true);
    }

    @Bean
    public Binding forgotPasswordBinding(Queue forgotPasswordQueue, TopicExchange emailExchange) {
        return BindingBuilder
                .bind(forgotPasswordQueue)
                .to(emailExchange)
                .with(EMAIL_FORGOT_PASSWORD_ROUTING_KEY);
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
