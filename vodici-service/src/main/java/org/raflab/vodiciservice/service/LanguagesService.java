package org.raflab.vodiciservice.service;

import org.raflab.vodiciservice.converter.GuideConverter;
import org.raflab.vodiciservice.converter.LanguagesConverter;
import org.raflab.vodiciservice.dto.request.GuideRequest;
import org.raflab.vodiciservice.dto.request.LanguagesRequest;
import org.raflab.vodiciservice.dto.response.GuideResponse;
import org.raflab.vodiciservice.dto.response.LanguagesResponse;
import org.raflab.vodiciservice.model.Languages;
import org.raflab.vodiciservice.repositories.GuidesRepository;
import org.raflab.vodiciservice.repositories.LanguagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguagesService {
    @Autowired
    private LanguagesRepository languagesRepository;
    public void save(LanguagesRequest languagesRequest) {
        languagesRepository.save(LanguagesConverter.toLanguage(languagesRequest));
    }
    public List<LanguagesResponse> getAllLanguages() {
        return LanguagesConverter.toLanguagesResponseList(languagesRepository.findAll());
    }

    public LanguagesResponse findbyid(Long id){
        return LanguagesConverter.toLanguagesResponse(languagesRepository.findById(id).get());
    }

    public void updateById(LanguagesRequest languagesRequest, Long id){

        if(languagesRepository.existsById(id)){
            Languages languages=languagesRepository.findById(id).get();
            languages.setLanguage_name(languagesRequest.getLanguage_name());
            languages.setLanguage_code(languagesRequest.getLanguage_code());
            languagesRepository.save(languages);
        }else {
            languagesRepository.save(LanguagesConverter.toLanguage(languagesRequest));
        }
    }

    public void deleteById(Long id){
        languagesRepository.deleteById(id);
    }
}
