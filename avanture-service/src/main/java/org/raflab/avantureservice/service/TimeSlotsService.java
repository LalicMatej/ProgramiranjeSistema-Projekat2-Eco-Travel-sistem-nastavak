package org.raflab.avantureservice.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.raflab.avantureservice.client.VodiciClient;
import org.raflab.avantureservice.config.RabbitMQConfig;
import org.raflab.avantureservice.converter.AdventureCategoriesConverter;
import org.raflab.avantureservice.converter.AdventuresConverter;
import org.raflab.avantureservice.converter.TimeSlotsConverter;
import org.raflab.avantureservice.dto.request.TimeSlotsRequest;
import org.raflab.avantureservice.dto.response.GuideResponse;
import org.raflab.avantureservice.dto.response.TimeSlotsResponse;
import org.raflab.avantureservice.dto.response.VacationsResponse;
import org.raflab.avantureservice.model.AdventureCategories;
import org.raflab.avantureservice.model.Adventures;
import org.raflab.avantureservice.model.TimeSlots;
import org.raflab.avantureservice.observer.AnnouncementsSubscriber;
import org.raflab.avantureservice.observer.TimeSlotPublisher;
import org.raflab.avantureservice.repositories.AdventureCategoriesRepository;
import org.raflab.avantureservice.repositories.AdventuresRepository;
import org.raflab.avantureservice.repositories.TimeSlotsRepository;
import org.raflab.sharedevents.AdventureTimeSlotFeedbackEvent;
import org.raflab.sharedevents.AdventureTimeSlotUpdatedEvent;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TimeSlotsService implements TimeSlotPublisher {
    @Autowired
    private TimeSlotsRepository timeSlotsRepository;
    @Autowired
    private AdventuresRepository adventuresRepository;
    @Autowired
    private AdventureCategoriesRepository adventureCategoriesRepository;

    private final List<AnnouncementsSubscriber> subscribers = new ArrayList<>();

    private final VodiciClient vodiciClient;
    private final RabbitTemplate rabbitTemplate;

    public TimeSlotsService(VodiciClient vodiciClient, RabbitTemplate rabbitTemplate, List<AnnouncementsSubscriber> subscriber) {
        this.vodiciClient = vodiciClient;
        this.rabbitTemplate = rabbitTemplate;
        this.subscribers.addAll(subscriber);
    }

    @CircuitBreaker(name = "vodiciServiceCB", fallbackMethod = "fallBackCreateTimeSlot")
    @RateLimiter(name = "vodiciServiceRateLimit", fallbackMethod = "fallBackRateLimitTimeSlot")
    @Retry(name = "vodiciServiceCB")
    public void save(TimeSlotsRequest timeSlotsRequest) {
        TimeSlots timeSlots=TimeSlotsConverter.toTimeSlots(timeSlotsRequest);
        Optional<Adventures> adventures=adventuresRepository.findByTitle(timeSlotsRequest.getAdventuresRequest().getTitle());

        GuideResponse guideResponse=vodiciClient.getGuideById(timeSlotsRequest.getAdventuresRequest().getGuide_id());
        List<VacationsResponse> vacationsList=vodiciClient.getVacationsByImeAndPrezime(guideResponse.getFirst_name(),guideResponse.getLast_name());

        for(VacationsResponse vacationsResponse:vacationsList){
            if(timeSlots.getStartTime().isBefore(vacationsResponse.getEndDate()) && timeSlots.getEndTime().isAfter(vacationsResponse.getStartDate())){
                return;
            }
        }

        if(adventures.isPresent()){
            timeSlots.setAdventures(adventures.get());
        }else {
            Adventures newAdventures= AdventuresConverter.toAdventures(timeSlotsRequest.getAdventuresRequest());
            newAdventures.setGuide_id(timeSlotsRequest.getAdventuresRequest().getGuide_id());
            adventuresRepository.save(newAdventures);
            timeSlots.setAdventures(newAdventures);
        }
        timeSlotsRepository.save(timeSlots);

        notifySubscriber(timeSlots);


    }
    public List<TimeSlotsResponse> getAllTimeSlots() {
        return TimeSlotsConverter.toTimeSlotsResponseList(timeSlotsRepository.findAll());
    }

    public void updateById(TimeSlotsRequest timeSlotsRequest, Long id){

        if(timeSlotsRepository.existsById(id)){
            TimeSlots timeSlots=timeSlotsRepository.findById(id).get();
            LocalDate oldStartDate = timeSlots.getStartTime();
            LocalDate oldEndDate = timeSlots.getEndTime();
            timeSlots.setStartTime(timeSlotsRequest.getStartTime());
            timeSlots.setEndTime(timeSlotsRequest.getEndTime());
            timeSlots.setCurrent_occupancy(timeSlotsRequest.getCurrent_occupancy());
            timeSlots.setMax_capacity(timeSlotsRequest.getMax_capacity());
            Adventures adventures;
            Optional<Adventures> adv=adventuresRepository.findByTitle(timeSlotsRequest.getAdventuresRequest().getTitle());
            if(adv.isPresent()){
                adventures=adv.get();
            }else{
                adventures=AdventuresConverter.toAdventures(timeSlotsRequest.getAdventuresRequest());
            }
            Optional<AdventureCategories> adcg=adventureCategoriesRepository.findByName(timeSlotsRequest.getAdventuresRequest().getAdventure_categoriesRequest().getName());
            if(adcg.isPresent()){
                adventures.setAdventure_categories(adcg.get());
            }else {
                AdventureCategories newAdventureCategories=AdventureCategoriesConverter.toAdventureCategories(timeSlotsRequest.getAdventuresRequest().getAdventure_categoriesRequest());
                adventureCategoriesRepository.save(newAdventureCategories);
                adventures.setAdventure_categories(newAdventureCategories);
            }
            adventuresRepository.save(adventures);
            timeSlots.setAdventures(adventures);
            TimeSlots savedTimeSlot = timeSlotsRepository.save(timeSlots);
            publishTimeSlotUpdated(savedTimeSlot, oldStartDate, oldEndDate);
        }else {
            TimeSlots savedTimeSlot = timeSlotsRepository.save(TimeSlotsConverter.toTimeSlots(timeSlotsRequest));
            publishTimeSlotUpdated(savedTimeSlot, null, null);
        }
    }

    private void publishTimeSlotUpdated(TimeSlots timeSlots, LocalDate oldStartDate, LocalDate oldEndDate) {
        Long adventureId = timeSlots.getAdventures() != null ? timeSlots.getAdventures().getId() : null;
        AdventureTimeSlotUpdatedEvent event = new AdventureTimeSlotUpdatedEvent(
                timeSlots.getId(),
                adventureId,
                oldStartDate,
                oldEndDate,
                timeSlots.getStartTime(),
                timeSlots.getEndTime()
        );

        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.AVANTURE_EXCHANGE,
                    RabbitMQConfig.TIMESLOT_UPDATED_ROUTING_KEY,
                    event
            );
            System.out.println("[RabbitMQ] Objavljen timeslot.updated za timeSlotId=" + timeSlots.getId());
        } catch (AmqpException e) {
            System.out.println("[RabbitMQ] Nije moguce objaviti timeslot.updated za timeSlotId="
                    + timeSlots.getId() + ": " + e.getMessage());
        }
    }

    @Transactional
    public void applyTimeSlotFeedback(AdventureTimeSlotFeedbackEvent event) {
        if (event.getTimeSlotId() == null) {
            return;
        }

        timeSlotsRepository.findById(event.getTimeSlotId()).ifPresent(timeSlot -> {
            timeSlot.setFeedbackStatus(event.getStatus());
            timeSlot.setFeedbackMessage(event.getMessage());
            timeSlotsRepository.save(timeSlot);
            System.out.println("[RabbitMQ] Feedback za timeSlotId=" + event.getTimeSlotId()
                    + " upisan kao " + event.getStatus());
        });
    }

    public TimeSlotsResponse findbyid(Long id){
        return TimeSlotsConverter.toTimeSlotResponse(timeSlotsRepository.findById(id).get());
    }

    public void deleteById(Long id){
        timeSlotsRepository.deleteById(id);
    }
    public void fallBackCreateTimeSlot(TimeSlotsRequest timeSlotsRequest, Throwable t) {
        if (t instanceof feign.FeignException.NotFound) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Nemoguće proveriti dostupnost: Vodič sa ID-jem " +
                            timeSlotsRequest.getAdventuresRequest().getGuide_id() +
                            " nije pronađen ili ne postoje podaci o njegovim odmorima."
            );
        }

        if (t instanceof org.springframework.web.server.ResponseStatusException) {
            throw (org.springframework.web.server.ResponseStatusException) t;
        }

        throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE,
                "Sistemi za proveru vodiča i odmora su trenutno nedostupni. Termin ne može biti rezervisan."
        );
    }

    public void fallBackRateLimitTimeSlot(TimeSlotsRequest timeSlotsRequest, Throwable t) {
        throw new ResponseStatusException(
                HttpStatus.TOO_MANY_REQUESTS,
                "Previse zahteva prema sistemu vodica. Pokusajte ponovo malo kasnije."
        );
    }

    @Override
    public void addSubscriber(AnnouncementsSubscriber subscriber) {
        if (subscriber != null && !subscribers.contains(subscriber)) {
            subscribers.add(subscriber);
        }
    }

    @Override
    public void removeSubscriber(AnnouncementsSubscriber subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public void notifySubscriber(TimeSlots timeSlots) {
        for(AnnouncementsSubscriber subscriber : subscribers){
            subscriber.update(timeSlots);
        }
    }

}
