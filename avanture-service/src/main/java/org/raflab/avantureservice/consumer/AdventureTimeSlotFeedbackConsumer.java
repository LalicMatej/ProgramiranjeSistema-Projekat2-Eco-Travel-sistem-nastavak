package org.raflab.avantureservice.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.raflab.avantureservice.config.RabbitMQConfig;
import org.raflab.avantureservice.service.TimeSlotsService;
import org.raflab.sharedevents.AdventureTimeSlotFeedbackEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdventureTimeSlotFeedbackConsumer {
    private final TimeSlotsService timeSlotsService;

    @RabbitListener(queues = RabbitMQConfig.AVANTURE_TIMESLOT_FEEDBACK_QUEUE)
    public void onTimeSlotFeedback(AdventureTimeSlotFeedbackEvent event) {
        log.info("[RabbitMQ] Primljen timeslot feedback {} za timeSlotId={}",
                event.getStatus(), event.getTimeSlotId());
        timeSlotsService.applyTimeSlotFeedback(event);
    }
}
