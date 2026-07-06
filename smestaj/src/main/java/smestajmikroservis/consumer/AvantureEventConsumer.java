package smestajmikroservis.consumer;

import com.rabbitmq.client.Channel;
import org.raflab.sharedevents.AdventureTimeSlotFeedbackEvent;
import org.raflab.sharedevents.AdventureTimeSlotUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import smestajmikroservis.config.RabbitMQConfig;
import smestajmikroservis.services.AvailabilityCalendarService;

import java.io.IOException;

@Component
public class AvantureEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(AvantureEventConsumer.class);

    @Autowired
    private AvailabilityCalendarService availabilityCalendarService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.SMESTAJ_TIMESLOT_UPDATED_QUEUE)
    public void onTimeSlotUpdated(AdventureTimeSlotUpdatedEvent event, Channel channel,
                                  @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        log.info("[RabbitMQ] Primljen timeslot.updated za timeSlotId={}", event.getTimeSlotId());
        try {
            availabilityCalendarService.updateReservationFromTimeSlot(event);
            sendFeedback(event.getTimeSlotId(), "CONFIRMED", "Rezervacija smestaja je pomerena.");
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("[RabbitMQ] Greska pri obradi timeslot.updated za timeSlotId={}: {}",
                    event.getTimeSlotId(), e.getMessage());
            sendFeedback(event.getTimeSlotId(), "REJECTED", e.getMessage());
            channel.basicNack(tag, false, true);
        }
    }

    private void sendFeedback(Long timeSlotId, String status, String message) {
        AdventureTimeSlotFeedbackEvent feedbackEvent = new AdventureTimeSlotFeedbackEvent(
                timeSlotId,
                status,
                message
        );
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.TIMESLOT_FEEDBACK_EXCHANGE,
                RabbitMQConfig.TIMESLOT_FEEDBACK_ROUTING_KEY,
                feedbackEvent
        );
        log.info("[RabbitMQ] Poslat timeslot feedback {} za timeSlotId={}", status, timeSlotId);
    }
}
