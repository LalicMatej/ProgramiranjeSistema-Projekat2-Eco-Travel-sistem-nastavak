package org.raflab.avantureservice.converter;

import org.raflab.avantureservice.dto.request.AdventureCategoriesRequest;
import org.raflab.avantureservice.dto.request.AdventureReviewsRequest;
import org.raflab.avantureservice.dto.request.AdventuresRequest;
import org.raflab.avantureservice.dto.response.AdventureCategoriesResponse;
import org.raflab.avantureservice.dto.response.AdventuresResponse;
import org.raflab.avantureservice.model.AdventureCategories;
import org.raflab.avantureservice.model.AdventureReviews;
import org.raflab.avantureservice.model.Adventures;

import java.util.ArrayList;
import java.util.List;

public class AdventuresConverter {
    public static Adventures toAdventures(AdventuresRequest adventuresRequest) {
        Adventures adventures = new Adventures();
        adventures.setTitle(adventuresRequest.getTitle());
        adventures.setDescription(adventuresRequest.getDescription());
        adventures.setBase_price(adventuresRequest.getBase_price());
        adventures.setDifficulty_level(adventuresRequest.getDifficulty_level());
        adventures.setGuideLevelRequired(adventuresRequest.getGuideLevelRequired());

        return adventures;
    }
    public static AdventuresResponse toAdventuresResponse(Adventures adventures){
        AdventuresResponse adventuresResponse = new AdventuresResponse();
        adventuresResponse.setId(adventures.getId());
        adventuresResponse.setTitle(adventures.getTitle());
        adventuresResponse.setGuide_id(adventures.getGuide_id());
        adventuresResponse.setDescription(adventures.getDescription());
        adventuresResponse.setBase_price(adventures.getBase_price());
        adventuresResponse.setDifficulty_level(adventures.getDifficulty_level());
        adventuresResponse.setGuideLevelRequired(adventures.getGuideLevelRequired());
        adventuresResponse.setAdventureCategoriesResponse(AdventureCategoriesConverter.toAdventureCategoriesResponse(adventures.getAdventure_categories()));

        return adventuresResponse;
    }

    public static List<AdventuresResponse> toAdventuresResponseList(List<Adventures> adventuresList){
        List<AdventuresResponse> adventuresResponseList = new ArrayList<>();
        for(Adventures ad:adventuresList){
            adventuresResponseList.add(toAdventuresResponse(ad));
        }
        return adventuresResponseList;
    }
}
