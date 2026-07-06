package org.raflab.vodiciservice.service;

import org.raflab.vodiciservice.converter.CertificationsConverter;
import org.raflab.vodiciservice.converter.GuideConverter;
import org.raflab.vodiciservice.converter.LanguagesConverter;
import org.raflab.vodiciservice.converter.VacationsConverter;
import org.raflab.vodiciservice.dto.request.LanguagesRequest;
import org.raflab.vodiciservice.dto.request.VacationsRequest;
import org.raflab.vodiciservice.dto.response.CertificationsResponse;
import org.raflab.vodiciservice.dto.response.LanguagesResponse;
import org.raflab.vodiciservice.dto.response.VacationsResponse;
import org.raflab.vodiciservice.model.Guides;
import org.raflab.vodiciservice.model.Vacations;
import org.raflab.vodiciservice.repositories.GuidesRepository;
import org.raflab.vodiciservice.repositories.LanguagesRepository;
import org.raflab.vodiciservice.repositories.VacationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class VacationsService {
    @Autowired
    private VacationsRepository vacationsRepository;
    @Autowired
    private GuidesRepository guidesRepository;
    public void save(VacationsRequest vacationsRequest) {
        Vacations vacations=VacationsConverter.toVacation(vacationsRequest);
        Guides guide=guidesRepository.getByFirstNameAndLastName(vacationsRequest.getGuideRequest().getFirst_name(),vacationsRequest.getGuideRequest().getLast_name());
        if(guide!=null){
            vacations.setGuides(guide);
        }else{
            Guides newGuides= GuideConverter.toGuides(vacationsRequest.getGuideRequest());
            vacations.setGuides(newGuides);
            guidesRepository.save(newGuides);
        }
        validateVacationData(vacations);

        vacationsRepository.save(vacations);
    }
    public List<VacationsResponse> getAllVacations() {
        return VacationsConverter.toVacationResponseList(vacationsRepository.findAll());
    }

    public VacationsResponse findbyid(Long id){
        return VacationsConverter.toVacationResponse(vacationsRepository.findById(id).get());
    }

    public void updateById(VacationsRequest vacationsRequest, Long id){

        if(vacationsRepository.existsById(id)){
            Vacations vacations =vacationsRepository.findById(id).get();
            vacations.setStartDate(vacationsRequest.getStartDate());
            vacations.setEndDate(vacationsRequest.getEndDate());
            vacations.setDestination(vacationsRequest.getDestination());
            vacations.setGuides(GuideConverter.toGuides(vacationsRequest.getGuideRequest()));
            vacationsRepository.save(vacations);
        }else {
            vacationsRepository.save(VacationsConverter.toVacation(vacationsRequest));
        }
    }

    public void deleteById(Long id){
        vacationsRepository.deleteById(id);
    }
    public List<VacationsResponse> getVacationsByImeAndPrezime(String ime, String prezime){
        return VacationsConverter.toVacationResponseList(vacationsRepository.findVacationsByImeAndPrezime(ime,prezime));
    }

    public void validateVacationData(Vacations vacations){
        if(vacations.getStartDate()==null){
            throw new RuntimeException("Polje startDate u vacation nije uneseno");
        }
        if(vacations.getEndDate()==null){
            throw new RuntimeException("Polje endDate u vacation nije uneseno");
        }
        if(vacations.getDestination()==null || vacations.getDestination().isEmpty()){
            throw new RuntimeException("Polje destination u vacation nije uneseno");
        }
        if(vacations.getGuides()==null){
            throw new RuntimeException("Polje guideRequest u vacation nije uneseno");
        }
    }
}
