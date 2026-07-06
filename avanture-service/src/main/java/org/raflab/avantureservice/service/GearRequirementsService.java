package org.raflab.avantureservice.service;

import org.raflab.avantureservice.converter.AdventureReviewsConverter;
import org.raflab.avantureservice.converter.AdventuresConverter;
import org.raflab.avantureservice.converter.GearRequirementsConverter;
import org.raflab.avantureservice.dto.request.AdventureReviewsRequest;
import org.raflab.avantureservice.dto.request.AdventuresRequest;
import org.raflab.avantureservice.dto.request.GearRequirementsRequest;
import org.raflab.avantureservice.dto.response.AdventuresResponse;
import org.raflab.avantureservice.dto.response.GearRequirementsResponse;
import org.raflab.avantureservice.model.Adventures;
import org.raflab.avantureservice.model.GearRequirements;
import org.raflab.avantureservice.repositories.AdventuresRepository;
import org.raflab.avantureservice.repositories.GearRequirementsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GearRequirementsService {
    @Autowired
    private GearRequirementsRepository gearRequirementsRepository;
    @Autowired
    private AdventuresRepository adventuresRepository;
    public void save(GearRequirementsRequest gearRequirementsRequest) {
        GearRequirements gearRequirements=GearRequirementsConverter.toGearRequirement(gearRequirementsRequest);
        Optional<Adventures> adventures=adventuresRepository.findByTitle(gearRequirementsRequest.getAdventuresRequest().getTitle());

        if(adventures.isPresent()){
            gearRequirements.setAdventures(adventures.get());
        }else {
            Adventures newAdventures=AdventuresConverter.toAdventures(gearRequirementsRequest.getAdventuresRequest());
            adventuresRepository.save(newAdventures);
            gearRequirements.setAdventures(newAdventures);
        }

        gearRequirementsRepository.save(gearRequirements);
    }
    public List<GearRequirementsResponse> getAllGearRequirements() {
        return GearRequirementsConverter.toGearRequirementsResponseList(gearRequirementsRepository.findAll());
    }

    public void updateById(GearRequirementsRequest gearRequirementsRequest, Long id){

        if(gearRequirementsRepository.existsById(id)){
            GearRequirements gearRequirements=gearRequirementsRepository.findById(id).get();
            gearRequirements.setItem_name(gearRequirementsRequest.getItem_name());
            gearRequirements.set_mandatory(gearRequirements.is_mandatory());
            gearRequirements.setAdventures(AdventuresConverter.toAdventures(gearRequirementsRequest.getAdventuresRequest()));
            gearRequirementsRepository.save(gearRequirements);
        }else {
            gearRequirementsRepository.save(GearRequirementsConverter.toGearRequirement(gearRequirementsRequest));
        }
    }

    public GearRequirementsResponse findbyid(Long id){
        return GearRequirementsConverter.toGearRequirementResponse(gearRequirementsRepository.findById(id).get());
    }

    public void deleteById(Long id){
        gearRequirementsRepository.deleteById(id);
    }
}
