package org.raflab.vodiciservice.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.raflab.sharedevents.AdventureReviewFeedbackEvent;
import org.raflab.sharedevents.AdventureReviewCreatedEvent;
import org.raflab.vodiciservice.config.RabbitMQConfig;
import org.raflab.vodiciservice.service.GuideService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdventureReviewEventConsumer {
    private final GuideService guideService;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.VODICI_REVIEW_CREATED_QUEUE)
    public void onReviewCreated(AdventureReviewCreatedEvent event) {
        log.info("[RabbitMQ] Primljen review.created za recenziju id={}, vodic id={}, ocena={}",
                event.getReviewId(), event.getGuideId(), event.getRating());
        try {
            boolean updated = guideService.applyAdventureReview(event);
            if (updated) {
                sendFeedback(event, "CONFIRMED", "Rating vodica je azuriran.");
            } else {
                sendFeedback(event, "REJECTED", "Vodic ili ocena nisu pronadjeni.");
            }
        } catch (Exception e) {
            sendFeedback(event, "REJECTED", e.getMessage());
        }
    }

    private void sendFeedback(AdventureReviewCreatedEvent event, String status, String message) {
        AdventureReviewFeedbackEvent feedbackEvent = new AdventureReviewFeedbackEvent(
                event.getReviewId(),
                event.getGuideId(),
                status,
                message
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.REVIEW_FEEDBACK_EXCHANGE,
                RabbitMQConfig.REVIEW_FEEDBACK_ROUTING_KEY,
                feedbackEvent
        );
        log.info("[RabbitMQ] Poslat feedback {} za recenziju id={}", status, event.getReviewId());
    }
}
