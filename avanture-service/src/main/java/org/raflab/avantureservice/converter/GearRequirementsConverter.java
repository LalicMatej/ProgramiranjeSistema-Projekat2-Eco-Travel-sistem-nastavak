package org.raflab.avantureservice.converter;

import org.raflab.avantureservice.dto.request.AdventureReviewsRequest;
import org.raflab.avantureservice.dto.request.AdventuresRequest;
import org.raflab.avantureservice.dto.request.GearRequirementsRequest;
import org.raflab.avantureservice.dto.response.AdventuresResponse;
import org.raflab.avantureservice.dto.response.GearRequirementsResponse;
import org.raflab.avantureservice.model.AdventureReviews;
import org.raflab.avantureservice.model.Adventures;
import org.raflab.avantureservice.model.GearRequirements;

import java.util.ArrayList;
import java.util.List;

public class GearRequirementsConverter {
    public static GearRequirements toGearRequirement(GearRequirementsRequest gearRequirementsRequest) {
        GearRequirements gearRequirements = new GearRequirements();
        gearRequirements.set_mandatory(gearRequirementsRequest.is_mandatory());
        gearRequirements.setItem_name(gearRequirementsRequest.getItem_name());

        return gearRequirements;
    }
    public static GearRequirementsResponse toGearRequirementResponse(GearRequirements gearRequirements){
        GearRequirementsResponse gearRequirementsResponse = new GearRequirementsResponse();
        gearRequirementsResponse.setId(gearRequirements.getId());
        gearRequirementsResponse.setItem_name(gearRequirements.getItem_name());
        gearRequirementsResponse.set_mandatory(gearRequirements.is_mandatory());
        gearRequirementsResponse.setAdventuresResponse(AdventuresConverter.toAdventuresResponse(gearRequirements.getAdventures()));

        return gearRequirementsResponse;
    }

    public static List<GearRequirementsResponse> toGearRequirementsResponseList(List<GearRequirements> gearRequirementsList){
        List<GearRequirementsResponse> gearRequirementsResponseList = new ArrayList<>();
        for(GearRequirements gr:gearRequirementsList){
            gearRequirementsResponseList.add(toGearRequirementResponse(gr));
        }
        return gearRequirementsResponseList;
    }
}
