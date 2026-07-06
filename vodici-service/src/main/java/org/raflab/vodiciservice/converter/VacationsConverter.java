package org.raflab.vodiciservice.converter;

import org.raflab.vodiciservice.dto.request.LanguagesRequest;
import org.raflab.vodiciservice.dto.request.VacationsRequest;
import org.raflab.vodiciservice.dto.response.LanguagesResponse;
import org.raflab.vodiciservice.dto.response.VacationsResponse;
import org.raflab.vodiciservice.model.Languages;
import org.raflab.vodiciservice.model.Vacations;

import java.util.ArrayList;
import java.util.List;

public class VacationsConverter {
    public static Vacations toVacation(VacationsRequest vacationsRequest) {
        Vacations vacations = new Vacations();
        vacations.setStartDate(vacationsRequest.getStartDate());
        vacations.setEndDate(vacationsRequest.getEndDate());
        vacations.setDestination(vacationsRequest.getDestination());

        return vacations;
    }
    public static VacationsResponse toVacationResponse(Vacations vacations) {
        VacationsResponse vacationsResponse = new VacationsResponse();
        vacationsResponse.setId(vacations.getId());
        vacationsResponse.setStartDate(vacations.getStartDate());
        vacationsResponse.setEndDate(vacations.getEndDate());
        vacationsResponse.setDestination(vacations.getDestination());
        vacationsResponse.setGuideResponse(GuideConverter.toGuidesResponse(vacations.getGuides()));

        return vacationsResponse;
    }

    public static List<VacationsResponse> toVacationResponseList(List<Vacations> vacationList){
        List<VacationsResponse> vacationResponseList = new ArrayList<>();
        for(Vacations vacations:vacationList){
            vacationResponseList.add(toVacationResponse(vacations));
        }
        return vacationResponseList;
    }
}
