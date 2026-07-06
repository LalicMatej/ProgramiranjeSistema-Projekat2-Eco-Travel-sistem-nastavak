package org.raflab.avantureservice.controller;

import jakarta.validation.Valid;
import org.raflab.avantureservice.dto.request.AdventureCategoriesRequest;
import org.raflab.avantureservice.dto.response.AdventureCategoriesResponse;
import org.raflab.avantureservice.service.AdventureCategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8083")
@RestController
@RequestMapping("/api/avanture")
@RefreshScope
public class AdventureCategoriesController {
    @Autowired
    private AdventureCategoriesService adventureCategoriesService;
    @PostMapping(path = "/adventurecategories/add")
    public void addAdventureCategories(@RequestBody @Valid AdventureCategoriesRequest adventureCategoriesRequest) {
        adventureCategoriesService.save(adventureCategoriesRequest);
    }

    @GetMapping(path = "/adventurecategories/all")
    public List<AdventureCategoriesResponse> getAllAdventureCategories() {
        return adventureCategoriesService.getAllAdventureCategories();
    }

    @GetMapping(path = "/adventurecategories/{id}")
    public AdventureCategoriesResponse getAdventureCategoriesById(@PathVariable Long id)
    {
        return adventureCategoriesService.findbyid(id);
    }

    @PutMapping(path = "/adventurecategories/update/{id}")
    public void updateAdventureCategories(@RequestBody @Valid AdventureCategoriesRequest adventureCategoriesRequest,@PathVariable Long id){
        adventureCategoriesService.updateById(adventureCategoriesRequest,id);
    }


    @DeleteMapping(path = "/adventurecategories/delete/{id}")
    public void deleteAdventureCategoriesbyId(@PathVariable Long id) {
        adventureCategoriesService.deleteById(id);
    }


}
