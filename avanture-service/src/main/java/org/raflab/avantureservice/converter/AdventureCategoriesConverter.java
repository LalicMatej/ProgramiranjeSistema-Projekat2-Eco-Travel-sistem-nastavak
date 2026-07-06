package org.raflab.avantureservice.converter;

import org.raflab.avantureservice.dto.request.AdventureCategoriesRequest;
import org.raflab.avantureservice.dto.response.AdventureCategoriesResponse;
import org.raflab.avantureservice.model.AdventureCategories;

import java.util.ArrayList;
import java.util.List;

public class AdventureCategoriesConverter {
    public static AdventureCategories toAdventureCategories(AdventureCategoriesRequest adventureCategoriesRequest) {
        AdventureCategories adventureCategories = new AdventureCategories();
        adventureCategories.setName(adventureCategoriesRequest.getName());
        adventureCategories.setDescription(adventureCategoriesRequest.getDescription());

        return adventureCategories;
    }
    public static AdventureCategoriesResponse toAdventureCategoriesResponse(AdventureCategories adventureCategories){
        AdventureCategoriesResponse adventureCategoriesResponse = new AdventureCategoriesResponse();
        adventureCategoriesResponse.setId(adventureCategories.getId());
        adventureCategoriesResponse.setName(adventureCategories.getName());
        adventureCategoriesResponse.setDescription(adventureCategories.getDescription());

        return adventureCategoriesResponse;
    }

    public static List<AdventureCategoriesResponse> toAdventureCategoriesResponseList(List<AdventureCategories> adventureCategoriesList){
        List<AdventureCategoriesResponse> adventureCategoriesResponseList = new ArrayList<>();
        for(AdventureCategories ac:adventureCategoriesList){
            adventureCategoriesResponseList.add(toAdventureCategoriesResponse(ac));
        }
        return adventureCategoriesResponseList;
    }
}
