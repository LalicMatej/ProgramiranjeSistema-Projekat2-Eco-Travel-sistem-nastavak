package org.raflab.vodiciservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {
    public static final String AVANTURE_EXCHANGE = "avanture.exchange";
    public static final String VODICI_REVIEW_CREATED_QUEUE = "vodici.review.created.queue";
    public static final String REVIEW_CREATED_ROUTING_KEY = "review.created";
    public static final String REVIEW_FEEDBACK_EXCHANGE = "review.feedback.exchange";
    public static final String AVANTURE_REVIEW_FEEDBACK_QUEUE = "avanture.review.feedback.queue";
    public static final String REVIEW_FEEDBACK_ROUTING_KEY = "review.feedback";

    @Bean
    public Queue reviewCreatedQueue() {
        return QueueBuilder.durable(VODICI_REVIEW_CREATED_QUEUE).build();
    }

    @Bean
    public TopicExchange avantureExchange() {
        return new TopicExchange(AVANTURE_EXCHANGE, true, false);
    }

    @Bean
    public Binding reviewCreatedBinding(Queue reviewCreatedQueue, TopicExchange avantureExchange) {
        return BindingBuilder.bind(reviewCreatedQueue)
                .to(avantureExchange)
                .with(REVIEW_CREATED_ROUTING_KEY);
    }

    @Bean
    public Queue reviewFeedbackQueue() {
        return QueueBuilder.durable(AVANTURE_REVIEW_FEEDBACK_QUEUE).build();
    }

    @Bean
    public TopicExchange reviewFeedbackExchange() {
        return new TopicExchange(REVIEW_FEEDBACK_EXCHANGE, true, false);
    }

    @Bean
    public Binding reviewFeedbackBinding(Queue reviewFeedbackQueue, TopicExchange reviewFeedbackExchange) {
        return BindingBuilder.bind(reviewFeedbackQueue)
                .to(reviewFeedbackExchange)
                .with(REVIEW_FEEDBACK_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        rabbitTemplate.setObservationEnabled(true);
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter,
            @Value("${spring.rabbitmq.listener.simple.auto-startup:true}") boolean autoStartup) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        factory.setObservationEnabled(true);
        factory.setAutoStartup(autoStartup);
        return factory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    // Kreiranje exchange, queues and binding prilikom pokretanja aplikacije
    @Bean
    @ConditionalOnProperty(name = "rabbitmq.initialize.enabled", havingValue = "true", matchIfMissing = true)
    public ApplicationRunner initializeQueues(RabbitAdmin rabbitAdmin) {
        return args -> {
            rabbitAdmin.initialize();
            System.out.println(">>> RABBITMQ REDOVI SU DEKLARISANI I SPREMNI!");
        };
    }
}
