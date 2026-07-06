package org.raflab.avantureservice.converter;

import org.raflab.avantureservice.dto.request.AdventureCategoriesRequest;
import org.raflab.avantureservice.dto.request.AdventureReviewsRequest;
import org.raflab.avantureservice.dto.response.AdventureCategoriesResponse;
import org.raflab.avantureservice.dto.response.AdventureReviewResponse;
import org.raflab.avantureservice.dto.response.AdventuresResponse;
import org.raflab.avantureservice.model.AdventureCategories;
import org.raflab.avantureservice.model.AdventureReviews;
import org.raflab.avantureservice.model.Adventures;

import java.util.ArrayList;
import java.util.List;

public class AdventureReviewsConverter {
    public static AdventureReviews toAdventureReviews(AdventureReviewsRequest adventureReviewsRequest) {
        AdventureReviews adventureReviews = new AdventureReviews();
        if(adventureReviewsRequest.getRating()<1 || adventureReviewsRequest.getRating()>5){
            throw   new RuntimeException("Rating mora biti izmedju 1 i 5.");
        }
        adventureReviews.setRating(adventureReviewsRequest.getRating());
        adventureReviews.setComment(adventureReviewsRequest.getComment());
        adventureReviews.setCreated_at(adventureReviewsRequest.getCreated_at());

        return adventureReviews;
    }
    public static AdventureReviewResponse toAdventuresReviewsResponse(AdventureReviews adventuresReviews){
        AdventureReviewResponse adventureReviewResponse = new AdventureReviewResponse();
        adventureReviewResponse.setId(adventuresReviews.getId());
        adventureReviewResponse.setRating(adventuresReviews.getRating());
        adventureReviewResponse.setComment(adventuresReviews.getComment());
        adventureReviewResponse.setCreated_at(adventuresReviews.getCreated_at());
        adventureReviewResponse.setFeedbackStatus(adventuresReviews.getFeedbackStatus());
        adventureReviewResponse.setFeedbackMessage(adventuresReviews.getFeedbackMessage());
        adventureReviewResponse.setAdventuresResponse(AdventuresConverter.toAdventuresResponse(adventuresReviews.getAdventures()));

        return adventureReviewResponse;
    }

    public static List<AdventureReviewResponse> toAdventureReviewsResponseList(List<AdventureReviews> adventureReviewsList){
        List<AdventureReviewResponse> adventureReviewsResponseList = new ArrayList<>();
        for(AdventureReviews ar:adventureReviewsList){
            adventureReviewsResponseList.add(toAdventuresReviewsResponse(ar));
        }
        return adventureReviewsResponseList;
    }
}
