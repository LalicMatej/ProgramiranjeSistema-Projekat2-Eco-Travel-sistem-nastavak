package org.raflab.vodiciservice.converter;

import org.raflab.vodiciservice.dto.request.CertificationsRequest;
import org.raflab.vodiciservice.dto.request.GuideRequest;
import org.raflab.vodiciservice.dto.response.GuideResponse;
import org.raflab.vodiciservice.model.Certifications;
import org.raflab.vodiciservice.model.Guides;

import java.util.ArrayList;
import java.util.List;

public class GuideConverter {
    public static Guides toGuides(GuideRequest guideRequest) {
        Guides guides = new Guides();
        guides.setFirst_name(guideRequest.getFirst_name());
        guides.setLast_name(guideRequest.getLast_name());
        guides.setBio(guideRequest.getBio());
        guides.setRating(guideRequest.getRating());

        return guides;
    }
    public static GuideResponse toGuidesResponse(Guides guides) {
        return GuideResponse.builder()
                .id(guides.getId())
                .first_name(guides.getFirst_name())
                .last_name(guides.getLast_name())
                .bio(guides.getBio())
                .rating(guides.getRating())
                .build();
    }

    public static List<GuideResponse> toGuidesResponseList(List<Guides> guidesList){
        List<GuideResponse> guideResponseList = new ArrayList<>();
        for(Guides gu:guidesList){
            guideResponseList.add(toGuidesResponse(gu));
        }
        return guideResponseList;
    }
}
