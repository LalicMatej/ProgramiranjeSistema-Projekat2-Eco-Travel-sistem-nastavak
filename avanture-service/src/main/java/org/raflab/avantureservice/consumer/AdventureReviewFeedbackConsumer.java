package org.raflab.avantureservice.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.raflab.avantureservice.config.RabbitMQConfig;
import org.raflab.avantureservice.service.AdventureReviewsService;
import org.raflab.sharedevents.AdventureReviewFeedbackEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdventureReviewFeedbackConsumer {
    private final AdventureReviewsService adventureReviewsService;

    @RabbitListener(queues = RabbitMQConfig.AVANTURE_REVIEW_FEEDBACK_QUEUE)
    public void onReviewFeedback(AdventureReviewFeedbackEvent event) {
        log.info("[RabbitMQ] Primljen feedback {} za recenziju id={}",
                event.getStatus(), event.getReviewId());
        adventureReviewsService.applyReviewFeedback(event);
    }
}
