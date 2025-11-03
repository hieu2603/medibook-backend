package com.sgu.notification_service.config;

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
    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";

    // Appointment Notifications
    public static final String NOTIFICATION_APPOINTMENT_SUCCESS_QUEUE = "notification.appointment-success.queue";
    public static final String NOTIFICATION_APPOINTMENT_SUCCESS_ROUTING_KEY = "NOTIFICATION.APPOINTMENT.SUCCESS";

    public static final String NOTIFICATION_APPOINTMENT_CANCELED_QUEUE = "notification.appointment-canceled.queue";
    public static final String NOTIFICATION_APPOINTMENT_CANCELED_ROUTING_KEY = "NOTIFICATION.APPOINTMENT.CANCELED";

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }

    // Appointment Success Queue + Binding
    @Bean
    public Queue appointmentSuccessQueue() {
        return new Queue(NOTIFICATION_APPOINTMENT_SUCCESS_QUEUE, true);
    }

    @Bean
    public Binding appointmentSuccessBinding(Queue appointmentSuccessQueue, TopicExchange notificationExchange) {
        return BindingBuilder
                .bind(appointmentSuccessQueue)
                .to(notificationExchange)
                .with(NOTIFICATION_APPOINTMENT_SUCCESS_ROUTING_KEY);
    }

    // Appointment Canceled Queue + Binding
    @Bean
    public Queue appointmentCanceledQueue() {
        return new Queue(NOTIFICATION_APPOINTMENT_CANCELED_QUEUE, true);
    }

    @Bean
    public Binding appointmentCanceledBinding(Queue appointmentCanceledQueue, TopicExchange notificationExchange) {
        return BindingBuilder
                .bind(appointmentCanceledQueue)
                .to(notificationExchange)
                .with(NOTIFICATION_APPOINTMENT_CANCELED_ROUTING_KEY);
    }


    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
