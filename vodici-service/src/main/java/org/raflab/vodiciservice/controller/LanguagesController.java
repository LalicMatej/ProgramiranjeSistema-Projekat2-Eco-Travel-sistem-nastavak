package org.raflab.vodiciservice.controller;

import jakarta.validation.Valid;
import org.raflab.vodiciservice.dto.request.GuideRequest;
import org.raflab.vodiciservice.dto.request.LanguagesRequest;
import org.raflab.vodiciservice.dto.response.GuideResponse;
import org.raflab.vodiciservice.dto.response.LanguagesResponse;
import org.raflab.vodiciservice.service.GuideService;
import org.raflab.vodiciservice.service.LanguagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8083")
@RestController
@RequestMapping("/api/vodici")
@RefreshScope
public class LanguagesController {
    @Autowired
    private LanguagesService languagesService;
    @PostMapping(path = "/languages/add")
    public void addLanguage(@RequestBody @Valid LanguagesRequest languagesRequest) {
        languagesService.save(languagesRequest);
    }

    @GetMapping(path = "/languages/all")
    public List<LanguagesResponse> getAllLanguages() {
        return languagesService.getAllLanguages();
    }

    @GetMapping(path = "/languages/{id}")
    public LanguagesResponse getLanguageById(@PathVariable Long id)
    {
        return languagesService.findbyid(id);
    }

    @PutMapping(path = "/languages/update/{id}")
    public void updateLanguage(@RequestBody @Valid LanguagesRequest languagesRequest, @PathVariable Long id){
        languagesService.updateById(languagesRequest,id);
    }

    @DeleteMapping(path = "/languages/delete/{id}")
    public void deleteLanguageById(@PathVariable Long id) {
        languagesService.deleteById(id);
    }


}
