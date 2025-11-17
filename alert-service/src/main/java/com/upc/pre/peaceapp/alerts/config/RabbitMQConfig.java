package com.upc.pre.peaceapp.alerts.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for RabbitMQ.
 * This class defines the necessary beans for communication with RabbitMQ,
 * including exchanges, queues, bindings, JSON message converters, and
 * the Rabbit listener container configuration.
 */
@Configuration
public class RabbitMQConfig {

    /**
     * Defines a {@link TopicExchange} for handling alert messages.
     * This exchange allows routing messages to multiple queues
     * based on routing key patterns.
     *
     * @return The {@link TopicExchange} named "alert.exchange".
     */
    @Bean
    public TopicExchange alertExchange() {
        return new TopicExchange("alert.exchange");
    }

    /**
     * Defines the specific queue for handling deleted alert events.
     * The queue is configured as durable ({@code true}).
     *
     * @return The {@link Queue} named "alert.deleted.queue".
     */
    @Bean
    public Queue alertDeletedQueue() {
        return new Queue("alert.deleted.queue", true);
    }

    /**
     * Creates a {@link Binding} to link the {@code alertDeletedQueue}
     * to the {@code alertExchange} using the routing key "alert.deleted".
     *
     * @param alertDeletedQueue The queue to be bound.
     * @param alertExchange The exchange to bind to.
     * @return The created {@link Binding}.
     */
    @Bean
    public Binding bindingAlertDeleted(Queue alertDeletedQueue, TopicExchange alertExchange) {
        return BindingBuilder.bind(alertDeletedQueue)
            .to(alertExchange)
            .with("alert.deleted");
    }

    /**
     * Configures the message converter for serializing and deserializing
     * messages using Jackson (JSON).
     * It registers the {@link JavaTimeModule} to handle Java 8 date/time types
     * and disables {@link SerializationFeature#WRITE_DATES_AS_TIMESTAMPS}
     * to write dates as ISO 8601 strings.
     *
     * @return A configured {@link Jackson2JsonMessageConverter}.
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return new Jackson2JsonMessageConverter(mapper);
    }

    /**
     * Configures the {@link SimpleRabbitListenerContainerFactory} for RabbitMQ listeners.
     * This allows listeners to automatically receive and convert JSON messages.
     *
     * @param connectionFactory The RabbitMQ connection factory.
     * @return The configured {@link SimpleRabbitListenerContainerFactory}.
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        return factory;
    }

    /**
     * Configures the {@link RabbitTemplate}, the main tool for sending messages to RabbitMQ.
     * It is assigned the JSON message converter.
     *
     * @param connectionFactory The RabbitMQ connection factory.
     * @return A configured {@link RabbitTemplate}.
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}