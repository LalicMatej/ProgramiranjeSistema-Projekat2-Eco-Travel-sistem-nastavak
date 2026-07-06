package org.raflab.vodiciservice.service;

import org.raflab.vodiciservice.converter.CertificationsConverter;
import org.raflab.vodiciservice.converter.GuideConverter;
import org.raflab.vodiciservice.dto.request.CertificationsRequest;
import org.raflab.vodiciservice.dto.request.GuideRequest;
import org.raflab.vodiciservice.dto.response.GuideResponse;
import org.raflab.sharedevents.AdventureReviewCreatedEvent;
import org.raflab.vodiciservice.model.Guides;
import org.raflab.vodiciservice.repositories.GuidesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GuideService {
    @Autowired
    private GuidesRepository guidesRepository;
    public void save(GuideRequest guideRequest) {
        guidesRepository.save(GuideConverter.toGuides(guideRequest));
    }
    public List<GuideResponse> getAllGuides() {
        return GuideConverter.toGuidesResponseList(guidesRepository.findAll());
    }

    public void updateById(GuideRequest guideRequest, Long id){

        if(guidesRepository.existsById(id)){
            Guides guides = guidesRepository.findById(id).get();
            guides.setFirst_name(guideRequest.getFirst_name());
            guides.setLast_name(guideRequest.getLast_name());
            guides.setBio(guideRequest.getBio());
            guides.setRating(guideRequest.getRating());
            guidesRepository.save(guides);
        }else {
            guidesRepository.save(GuideConverter.toGuides(guideRequest));
        }
    }

    public GuideResponse findbyid(Long id){
        return GuideConverter.toGuidesResponse(guidesRepository.findById(id).get());
    }

    public void deleteById(Long id){
        guidesRepository.deleteById(id);
    }

    @Transactional
    public boolean applyAdventureReview(AdventureReviewCreatedEvent event) {
        if (event.getGuideId() == null || event.getRating() == null) {
            return false;
        }

        Guides guide = guidesRepository.findById(event.getGuideId()).orElse(null);
        if (guide == null) {
            return false;
        }

        int currentCount = guide.getRatingCount() != null ? guide.getRatingCount() : 0;
        double currentRating = guide.getRating() != null ? guide.getRating() : 0.0;
        double newRating = ((currentRating * currentCount) + event.getRating()) / (currentCount + 1);

        guide.setRating(newRating);
        guide.setRatingCount(currentCount + 1);
        guidesRepository.save(guide);
        return true;
    }

    public Double getAverageRatingMethod(){
        List<Double> listaOcena=guidesRepository.getSumRating();
        Double grade=0.0;
        for(Double d:listaOcena){
            grade=grade+d;
        }
        return grade/listaOcena.size();
    }
}
