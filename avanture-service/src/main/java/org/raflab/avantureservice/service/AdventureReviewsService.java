package org.raflab.avantureservice.service;

import org.raflab.avantureservice.converter.AdventureCategoriesConverter;
import org.raflab.avantureservice.converter.AdventureReviewsConverter;
import org.raflab.avantureservice.converter.AdventuresConverter;
import org.raflab.avantureservice.config.RabbitMQConfig;
import org.raflab.avantureservice.dto.request.AdventureCategoriesRequest;
import org.raflab.avantureservice.dto.request.AdventureReviewsRequest;
import org.raflab.avantureservice.dto.response.AdventureCategoriesResponse;
import org.raflab.avantureservice.dto.response.AdventureReviewResponse;
import org.raflab.avantureservice.model.AdventureReviews;
import org.raflab.avantureservice.model.Adventures;
import org.raflab.avantureservice.repositories.AdventureCategoriesRepository;
import org.raflab.avantureservice.repositories.AdventureReviewsRepository;
import org.raflab.avantureservice.repositories.AdventuresRepository;
import org.raflab.sharedevents.AdventureReviewFeedbackEvent;
import org.raflab.sharedevents.AdventureReviewCreatedEvent;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdventureReviewsService {
    @Autowired
    private AdventureReviewsRepository adventureReviewsRepository;
    @Autowired
    private AdventuresRepository adventuresRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void save(AdventureReviewsRequest adventureReviewsRequest) {
        AdventureReviews adventureReviews = AdventureReviewsConverter.toAdventureReviews(adventureReviewsRequest);
        Optional<Adventures> adventures=adventuresRepository.findByTitle(adventureReviewsRequest.getAdventuresRequest().getTitle());

        if(adventures.isPresent()){
            adventureReviews.setAdventures(adventures.get());
        }else {
            Adventures newAdventures= AdventuresConverter.toAdventures(adventureReviewsRequest.getAdventuresRequest());
            newAdventures.setGuide_id(adventureReviewsRequest.getAdventuresRequest().getGuide_id());
            adventuresRepository.save(newAdventures);
            adventureReviews.setAdventures(newAdventures);
        }

        AdventureReviews savedReview = adventureReviewsRepository.save(adventureReviews);
        publishReviewCreated(savedReview);
    }

    public void applyReviewFeedback(AdventureReviewFeedbackEvent event) {
        if (event.getReviewId() == null) {
            return;
        }

        adventureReviewsRepository.findById(event.getReviewId()).ifPresent(review -> {
            review.setFeedbackStatus(event.getStatus());
            review.setFeedbackMessage(event.getMessage());
            adventureReviewsRepository.save(review);
            System.out.println("[RabbitMQ] Feedback za recenziju id=" + event.getReviewId()
                    + " upisan kao " + event.getStatus());
        });
    }

    private void publishReviewCreated(AdventureReviews adventureReviews) {
        Adventures adventure = adventureReviews.getAdventures();
        Long adventureId = adventure != null ? adventure.getId() : null;
        Long guideId = adventure != null ? adventure.getGuide_id() : null;

        AdventureReviewCreatedEvent event = new AdventureReviewCreatedEvent(
                adventureReviews.getId(),
                adventureId,
                guideId,
                adventureReviews.getRating(),
                adventureReviews.getComment(),
                adventureReviews.getCreated_at()
        );

        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.AVANTURE_EXCHANGE,
                    RabbitMQConfig.REVIEW_CREATED_ROUTING_KEY,
                    event
            );
            System.out.println("[RabbitMQ] Objavljen review.created za recenziju id=" + adventureReviews.getId());
        } catch (AmqpException e) {
            System.out.println("[RabbitMQ] Nije moguce objaviti review.created za recenziju id="
                    + adventureReviews.getId() + ": " + e.getMessage());
        }
    }
    public List<AdventureReviewResponse> getAllAdventureReviews() {
        return AdventureReviewsConverter.toAdventureReviewsResponseList(adventureReviewsRepository.findAll());
    }

    public AdventureReviewResponse findbyid(Long id){
        return AdventureReviewsConverter.toAdventuresReviewsResponse(adventureReviewsRepository.findById(id).get());
    }

    public void updateById(AdventureReviewsRequest adventureReviewsRequest,Long id){

        if(adventureReviewsRepository.existsById(id)){
            AdventureReviews adventureReviews=adventureReviewsRepository.findById(id).get();
            adventureReviews.setComment(adventureReviewsRequest.getComment());
            adventureReviews.setRating(adventureReviewsRequest.getRating());
            adventureReviews.setCreated_at(adventureReviewsRequest.getCreated_at());
            adventureReviews.setAdventures(AdventuresConverter.toAdventures(adventureReviewsRequest.getAdventuresRequest()));
            adventureReviewsRepository.save(adventureReviews);
        }else {
            adventureReviewsRepository.save(AdventureReviewsConverter.toAdventureReviews(adventureReviewsRequest));
        }
    }

    public void deleteById(Long id){
        adventureReviewsRepository.deleteById(id);
    }
}
