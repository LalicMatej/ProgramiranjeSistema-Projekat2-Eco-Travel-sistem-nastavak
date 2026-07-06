package odrzavanjemikroservis.config;

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

    public static final String ODRZAVANJE_EXCHANGE = "odrzavanje.exchange";
    public static final String SMESTAJ_EXCHANGE = "smestaj.exchange";
    public static final String DLX_EXCHANGE = "dlx.exchange";
    public static final String ODRZAVANJE_UNIT_DELETED_QUEUE = "odrzavanje.unit-deleted.queue";
    public static final String ODRZAVANJE_UNIT_DELETED_DLQ = "odrzavanje.unit-deleted.dlq";

    @Bean
    public TopicExchange odrzavanjeExchange() {
        return new TopicExchange(ODRZAVANJE_EXCHANGE, true, false);
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(DLX_EXCHANGE, true, false);
    }

    private TopicExchange smestajExchange() {
        return new TopicExchange(SMESTAJ_EXCHANGE, true, false);
    }

    @Bean
    public Queue unitDeletedQueue() {
        return QueueBuilder.durable(ODRZAVANJE_UNIT_DELETED_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", ODRZAVANJE_UNIT_DELETED_DLQ)
                .build();
    }

    @Bean
    public Queue unitDeletedDlq() {
        return QueueBuilder.durable(ODRZAVANJE_UNIT_DELETED_DLQ).build();
    }

    @Bean
    public Binding unitDeletedBinding() {
        return BindingBuilder.bind(unitDeletedQueue()).to(smestajExchange()).with("unit.deleted");
    }

    @Bean
    public Binding unitDeletedDlqBinding() {
        return BindingBuilder.bind(unitDeletedDlq()).to(dlxExchange()).with(ODRZAVANJE_UNIT_DELETED_DLQ);
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
