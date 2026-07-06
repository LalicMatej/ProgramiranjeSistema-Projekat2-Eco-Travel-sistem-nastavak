package org.raflab.avantureservice.service;

import org.raflab.avantureservice.converter.AdventureCategoriesConverter;
import org.raflab.avantureservice.dto.request.AdventureCategoriesRequest;
import org.raflab.avantureservice.dto.response.AdventureCategoriesResponse;
import org.raflab.avantureservice.model.AdventureCategories;
import org.raflab.avantureservice.repositories.AdventureCategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdventureCategoriesService {
    @Autowired
    private AdventureCategoriesRepository adventureCategoriesRepository;
    public void save(AdventureCategoriesRequest adventureCategoriesRequest) {
        AdventureCategories adventureCategories = AdventureCategoriesConverter.toAdventureCategories(adventureCategoriesRequest);

        validateAdventureCategoriesData(adventureCategories);

        adventureCategoriesRepository.save(adventureCategories);
    }
    public List<AdventureCategoriesResponse> getAllAdventureCategories() {
        return AdventureCategoriesConverter.toAdventureCategoriesResponseList(adventureCategoriesRepository.findAll());
    }

    public void updateById(AdventureCategoriesRequest adventureCategoriesRequest,Long id){

        if(adventureCategoriesRepository.existsById(id)){
            AdventureCategories adventureCategories=adventureCategoriesRepository.findById(id).get();
            adventureCategories.setName(adventureCategoriesRequest.getName());
            adventureCategories.setDescription(adventureCategoriesRequest.getDescription());
            adventureCategoriesRepository.save(adventureCategories);
        }else {
            adventureCategoriesRepository.save(AdventureCategoriesConverter.toAdventureCategories(adventureCategoriesRequest));
        }
    }

    public AdventureCategoriesResponse findbyid(Long id){
        return AdventureCategoriesConverter.toAdventureCategoriesResponse(adventureCategoriesRepository.findById(id).get());
    }

    public void deleteById(Long id){
        adventureCategoriesRepository.deleteById(id);
    }

    public void validateAdventureCategoriesData(AdventureCategories adventureCategories){
        if(adventureCategories.getName()==null || adventureCategories.getName().isEmpty()){
            throw new RuntimeException("Polje name u advenureCategories mora biti uneseno");
        }
        if(adventureCategories.getDescription()==null || adventureCategories.getDescription().isEmpty()){
            throw new RuntimeException("Polje destination u adventureCategories mora biti uneseno");
        }
    }
}
