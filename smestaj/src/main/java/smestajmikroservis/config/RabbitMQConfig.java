package smestajmikroservis.config;

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

@Configuration
public class RabbitMQConfig {

    public static final String SMESTAJ_EXCHANGE = "smestaj.exchange";
    public static final String ODRZAVANJE_EXCHANGE = "odrzavanje.exchange";
    public static final String AVANTURE_EXCHANGE = "avanture.exchange";
    public static final String TIMESLOT_FEEDBACK_EXCHANGE = "timeslot.feedback.exchange";
    public static final String DLX_EXCHANGE = "dlx.exchange";

    public static final String SMESTAJ_WORKORDER_CREATED_QUEUE = "smestaj.workorder-created.queue";
    public static final String SMESTAJ_WORKORDER_COMPLETED_QUEUE = "smestaj.workorder-completed.queue";
    public static final String SMESTAJ_TIMESLOT_UPDATED_QUEUE = "smestaj.timeslot.updated.queue";
    public static final String BOOKING_CREATED_QUEUE = "booking-created.queue";


    public static final String SMESTAJ_WORKORDER_CREATED_DLQ = "smestaj.workorder-created.dlq";
    public static final String SMESTAJ_WORKORDER_COMPLETED_DLQ = "smestaj.workorder-completed.dlq";
    public static final String BOOKING_CREATED_DLQ = "booking-created.dlq";

    public static final String TIMESLOT_UPDATED_ROUTING_KEY = "timeslot.updated";
    public static final String TIMESLOT_FEEDBACK_ROUTING_KEY = "timeslot.feedback";
    public static final String BOOKING_CREATED_ROUTING_KEY = "booking.created";

    @Bean
    public TopicExchange smestajExchange() {
        return new TopicExchange(SMESTAJ_EXCHANGE, true, false);
    }

    @Bean
    public TopicExchange odrzavanjeExchange() {
        return new TopicExchange(ODRZAVANJE_EXCHANGE, true, false);
    }

    @Bean
    public TopicExchange avantureExchange() {
        return new TopicExchange(AVANTURE_EXCHANGE, true, false);
    }

    @Bean
    public TopicExchange timeSlotFeedbackExchange() {
        return new TopicExchange(TIMESLOT_FEEDBACK_EXCHANGE, true, false);
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(DLX_EXCHANGE, true, false);
    }

    @Bean
    public Queue workorderCreatedQueue() {
        return QueueBuilder.durable(SMESTAJ_WORKORDER_CREATED_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", SMESTAJ_WORKORDER_CREATED_DLQ)
                .build();
    }

    @Bean
    public Queue workorderCompletedQueue() {
        return QueueBuilder.durable(SMESTAJ_WORKORDER_COMPLETED_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", SMESTAJ_WORKORDER_COMPLETED_DLQ)
                .build();
    }

    @Bean
    public Queue timeSlotUpdatedQueue() {
        return QueueBuilder.durable(SMESTAJ_TIMESLOT_UPDATED_QUEUE).build();
    }

    @Bean
    public Queue bookingCreatedQueue() {
        return QueueBuilder.durable(BOOKING_CREATED_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", BOOKING_CREATED_DLQ)
                .build();
    }

    @Bean
    public Queue workorderCreatedDlq() {
        return QueueBuilder.durable(SMESTAJ_WORKORDER_CREATED_DLQ).build();
    }

    @Bean
    public Queue workorderCompletedDlq() {
        return QueueBuilder.durable(SMESTAJ_WORKORDER_COMPLETED_DLQ).build();
    }

    @Bean
    public Queue bookingCreatedDlq() {
        return QueueBuilder.durable(BOOKING_CREATED_DLQ).build();
    }

    @Bean
    public Binding workorderCreatedBinding() {
        return BindingBuilder.bind(workorderCreatedQueue()).to(odrzavanjeExchange()).with("workorder.created");
    }

    @Bean
    public Binding workorderCompletedBinding() {
        return BindingBuilder.bind(workorderCompletedQueue()).to(odrzavanjeExchange()).with("workorder.completed");
    }

    @Bean
    public Binding timeSlotUpdatedBinding() {
        return BindingBuilder.bind(timeSlotUpdatedQueue()).to(avantureExchange()).with(TIMESLOT_UPDATED_ROUTING_KEY);
    }

    @Bean
    public Binding bookingCreatedBinding() {
        return BindingBuilder.bind(bookingCreatedQueue()).to(smestajExchange()).with(BOOKING_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding workorderCreatedDlqBinding() {
        return BindingBuilder.bind(workorderCreatedDlq()).to(dlxExchange()).with(SMESTAJ_WORKORDER_CREATED_DLQ);
    }

    @Bean
    public Binding workorderCompletedDlqBinding() {
        return BindingBuilder.bind(workorderCompletedDlq()).to(dlxExchange()).with(SMESTAJ_WORKORDER_COMPLETED_DLQ);
    }

    @Bean
    public Binding bookingCreatedDlqBinding() {
        return BindingBuilder.bind(bookingCreatedDlq()).to(dlxExchange()).with(BOOKING_CREATED_DLQ);
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
        template.setObservationEnabled(true);
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
        factory.setObservationEnabled(true);
        return factory;
    }
}
