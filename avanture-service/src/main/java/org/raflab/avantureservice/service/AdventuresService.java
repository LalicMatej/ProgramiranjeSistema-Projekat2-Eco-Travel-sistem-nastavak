package org.raflab.avantureservice.service;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.raflab.avantureservice.client.VodiciClient;
import org.raflab.avantureservice.converter.AdventureCategoriesConverter;
import org.raflab.avantureservice.converter.AdventuresConverter;
import org.raflab.avantureservice.dto.request.AdventuresRequest;
import org.raflab.avantureservice.dto.response.AdventuresResponse;
import org.raflab.avantureservice.dto.response.CertificationsResponse;
import org.raflab.avantureservice.dto.response.GuideResponse;
import org.raflab.avantureservice.exceptions.AdventureProcessingException;
import org.raflab.avantureservice.model.AdventureCategories;
import org.raflab.avantureservice.model.Adventures;
import org.raflab.avantureservice.repositories.AdventureCategoriesRepository;
import org.raflab.avantureservice.repositories.AdventuresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdventuresService {
    @Autowired
    private AdventuresRepository adventuresRepository;
    @Autowired
    private AdventureCategoriesRepository adventureCategoriesRepository;

    private final VodiciClient vodiciClient;

    @CircuitBreaker(name = "vodiciServiceCB", fallbackMethod = "fallBackCreateAdvenuture")
    @RateLimiter(name = "vodiciServiceRateLimit", fallbackMethod = "fallBackRateLimit")
    @Retry(name = "vodiciServiceCB")
    public void save(AdventuresRequest adventuresRequest) {
        Adventures adventures=AdventuresConverter.toAdventures(adventuresRequest);

        GuideResponse guideResponse= vodiciClient.getGuideById(adventuresRequest.getGuide_id());

        List<CertificationsResponse> cert=vodiciClient.getCertificationsByImeAndPrezime(guideResponse.getFirst_name(),guideResponse.getLast_name());
        
        for(CertificationsResponse cer:cert){
            if(cer.getCertificationLevel()>=adventuresRequest.getGuideLevelRequired()){
                adventures.setGuide_id(guideResponse.getId());
            }
        }
        if(adventures.getGuide_id()==null){
            return;
        }

        Optional<AdventureCategories> adcg=adventureCategoriesRepository.findByName(adventuresRequest.getAdventure_categoriesRequest().getName());
        if(adcg.isPresent()){
            adventures.setAdventure_categories(adcg.get());
        }else {
            AdventureCategories newAdventureCategories=AdventureCategoriesConverter.toAdventureCategories(adventuresRequest.getAdventure_categoriesRequest());
            adventureCategoriesRepository.save(newAdventureCategories);
            adventures.setAdventure_categories(newAdventureCategories);
        }

        adventuresRepository.save(adventures);
    }
    public List<AdventuresResponse> getAllAdventures() {
        return AdventuresConverter.toAdventuresResponseList(adventuresRepository.findAll());
    }

    public void updateById(AdventuresRequest adventuresRequest,Long id){

        if(adventuresRepository.existsById(id)){
            Adventures adventures=adventuresRepository.findById(id).get();
            adventures.setTitle(adventuresRequest.getTitle());
            adventures.setDescription(adventuresRequest.getDescription());
            adventures.setBase_price(adventuresRequest.getBase_price());
            adventures.setGuideLevelRequired(adventuresRequest.getGuideLevelRequired());
            adventures.setDifficulty_level(adventuresRequest.getDifficulty_level());
            adventures.setGuide_id(adventuresRequest.getGuide_id());
            adventures.setAdventure_categories(AdventureCategoriesConverter.toAdventureCategories(adventuresRequest.getAdventure_categoriesRequest()));

            adventuresRepository.save(adventures);
        }else {
            adventuresRepository.save(AdventuresConverter.toAdventures(adventuresRequest));
        }
    }

    public AdventuresResponse findbyid(Long id){
        return AdventuresConverter.toAdventuresResponse(adventuresRepository.findById(id).get());
    }

    public void deleteById(Long id){
        adventuresRepository.deleteById(id);
    }

    public Double getAveragePriceMethod(){
        List<Double> listaCena=adventuresRepository.getTotalPrice();
        Double cena=0.0;
        for(Double i:listaCena){
            cena=cena+i;
        }
        return cena/listaCena.size();
    }
    public void fallBackCreateAdvenuture(AdventuresRequest adventuresRequest, Throwable t) {
        if (t instanceof FeignException.NotFound) {
            throw new RuntimeException(

                    "Vodič sa ID-jem " + adventuresRequest.getGuide_id() + " nije pronađen u sistemu vodiča ili sertifikati za tog vodica nisu pronadjeni."+HttpStatus.NOT_FOUND

            );
        }


        throw new AdventureProcessingException(

                "Eksterni servis za vodiče je trenutno nedostupan. Molimo pokušajte kasnije.",
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }

        public void fallBackRateLimit(AdventuresRequest adventuresRequest, Throwable t) {
        throw new AdventureProcessingException("Previse zahteva! (Rate Limit).", HttpStatus.TOO_MANY_REQUESTS);
    }

}
