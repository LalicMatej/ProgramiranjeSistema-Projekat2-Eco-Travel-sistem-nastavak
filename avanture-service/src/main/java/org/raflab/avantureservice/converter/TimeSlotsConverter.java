package org.raflab.avantureservice.converter;

import org.raflab.avantureservice.dto.request.AdventureReviewsRequest;
import org.raflab.avantureservice.dto.request.GearRequirementsRequest;
import org.raflab.avantureservice.dto.request.TimeSlotsRequest;
import org.raflab.avantureservice.dto.response.GearRequirementsResponse;
import org.raflab.avantureservice.dto.response.TimeSlotsResponse;
import org.raflab.avantureservice.model.AdventureReviews;
import org.raflab.avantureservice.model.GearRequirements;
import org.raflab.avantureservice.model.TimeSlots;

import java.util.ArrayList;
import java.util.List;

public class TimeSlotsConverter {
    public static TimeSlots toTimeSlots(TimeSlotsRequest timeSlotsRequest) {
        TimeSlots timeSlots = new TimeSlots();
        timeSlots.setTermMark(timeSlotsRequest.getTermMark());
        timeSlots.setStartTime(timeSlotsRequest.getStartTime());
        timeSlots.setEndTime(timeSlotsRequest.getEndTime());
        if(timeSlotsRequest.getCurrent_occupancy()>timeSlotsRequest.getMax_capacity()){
            throw new RuntimeException("Broj ljudi je presao limit.");
        }
        timeSlots.setCurrent_occupancy(timeSlotsRequest.getCurrent_occupancy());
        timeSlots.setMax_capacity(timeSlotsRequest.getMax_capacity());

        return timeSlots;
        
    }
    public static TimeSlotsResponse toTimeSlotResponse(TimeSlots timeSlots) {
        TimeSlotsResponse timeSlotsResponse = new TimeSlotsResponse();
        timeSlotsResponse.setId(timeSlots.getId());
        timeSlotsResponse.setTermMark(timeSlots.getTermMark());
        timeSlotsResponse.setStartTime(timeSlots.getStartTime());
        timeSlotsResponse.setEndTime(timeSlots.getEndTime());
        timeSlotsResponse.setMax_capacity(timeSlots.getMax_capacity());
        timeSlotsResponse.setCurrent_occupancy(timeSlots.getCurrent_occupancy());
        timeSlotsResponse.setFeedbackStatus(timeSlots.getFeedbackStatus());
        timeSlotsResponse.setFeedbackMessage(timeSlots.getFeedbackMessage());
        timeSlotsResponse.setAdventuresResponse(AdventuresConverter.toAdventuresResponse(timeSlots.getAdventures()));

        return timeSlotsResponse;
    }

    public static List<TimeSlotsResponse> toTimeSlotsResponseList(List<TimeSlots> timeSlotsList){
        List<TimeSlotsResponse> timeSlotsResponseList = new ArrayList<>();
        for(TimeSlots ts:timeSlotsList){
            timeSlotsResponseList.add(toTimeSlotResponse(ts));
        }
        return timeSlotsResponseList;
    }
}
