package org.raflab.vodiciservice.converter;

import org.raflab.vodiciservice.dto.request.GuideRequest;
import org.raflab.vodiciservice.dto.request.LanguagesRequest;
import org.raflab.vodiciservice.dto.response.GuideResponse;
import org.raflab.vodiciservice.dto.response.LanguagesResponse;
import org.raflab.vodiciservice.model.Guides;
import org.raflab.vodiciservice.model.Languages;

import java.util.ArrayList;
import java.util.List;

public class LanguagesConverter {
    public static Languages toLanguage(LanguagesRequest languagesRequest) {
        Languages languages = new Languages();
        languages.setLanguage_name(languagesRequest.getLanguage_name());
        languages.setLanguage_code(languagesRequest.getLanguage_code());

        return languages;
    }
    public static LanguagesResponse toLanguagesResponse(Languages  languages) {
        LanguagesResponse languagesResponse = new LanguagesResponse();
        languagesResponse.setLanguage_name(languages.getLanguage_name());
        languagesResponse.setLanguage_code(languages.getLanguage_code());

        return languagesResponse;
    }

    public static List<LanguagesResponse> toLanguagesResponseList(List<Languages> languagesList){
        List<LanguagesResponse> languagesResponseList = new ArrayList<>();
        for(Languages la:languagesList){
            languagesResponseList.add(toLanguagesResponse(la));
        }
        return languagesResponseList;
    }
}
