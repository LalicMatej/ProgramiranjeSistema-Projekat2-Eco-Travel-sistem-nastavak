package org.example.financeservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String FINANCE_EXCHANGE = "finance.exchange";
    public static final String DLX_EXCHANGE = "dlx.exchange";

    public static final String BOOKING_INVOICE_REQUESTED_QUEUE = "finance.booking-invoice-requested.queue";
    public static final String BOOKING_INVOICE_REQUESTED_DLQ = "finance.booking-invoice-requested.dlq";

    public static final String BOOKING_INVOICE_REQUESTED_ROUTING_KEY = "booking.invoice-requested";

    @Bean
    public TopicExchange financeExchange() {
        return new TopicExchange(FINANCE_EXCHANGE, true, false);
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(DLX_EXCHANGE, true, false);
    }

    @Bean
    public Queue bookingInvoiceRequestedQueue() {
        return QueueBuilder.durable(BOOKING_INVOICE_REQUESTED_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", BOOKING_INVOICE_REQUESTED_DLQ)
                .build();
    }

    @Bean
    public Queue bookingInvoiceRequestedDlq() {
        return QueueBuilder.durable(BOOKING_INVOICE_REQUESTED_DLQ).build();
    }

    @Bean
    public Binding bookingInvoiceRequestedBinding() {
        return BindingBuilder.bind(bookingInvoiceRequestedQueue())
                .to(financeExchange())
                .with(BOOKING_INVOICE_REQUESTED_ROUTING_KEY);
    }

    @Bean
    public Binding bookingInvoiceRequestedDlqBinding() {
        return BindingBuilder.bind(bookingInvoiceRequestedDlq())
                .to(dlxExchange())
                .with(BOOKING_INVOICE_REQUESTED_DLQ);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                              Jackson2JsonMessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setDefaultRequeueRejected(false);
        return factory;
    }
}
