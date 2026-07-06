package org.example.bookingservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String SMESTAJ_EXCHANGE = "smestaj.exchange";
    public static final String FINANCE_EXCHANGE = "finance.exchange";
    public static final String BOOKING_CREATED_ROUTING_KEY = "booking.created";
    public static final String BOOKING_INVOICE_REQUESTED_ROUTING_KEY = "booking.invoice-requested";

    @Bean
    public TopicExchange smestajExchange() {
        return new TopicExchange(SMESTAJ_EXCHANGE, true, false);
    }

    @Bean
    public TopicExchange financeExchange() {
        return new TopicExchange(FINANCE_EXCHANGE, true, false);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
