package org.raflab.avantureservice.controller;

import jakarta.validation.Valid;
import org.raflab.avantureservice.dto.request.AdventureCategoriesRequest;
import org.raflab.avantureservice.dto.request.AdventureReviewsRequest;
import org.raflab.avantureservice.dto.response.AdventureCategoriesResponse;
import org.raflab.avantureservice.dto.response.AdventureReviewResponse;
import org.raflab.avantureservice.model.AdventureReviews;
import org.raflab.avantureservice.service.AdventureCategoriesService;
import org.raflab.avantureservice.service.AdventureReviewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8083")
@RestController
@RequestMapping("/api/avanture")
@RefreshScope
public class AdventureReviewsController {
    @Autowired
    private AdventureReviewsService adventureReviewsService;
    @PostMapping(path = "/adventurereviews/add")
    public void addAdventureReviews(@RequestBody @Valid AdventureReviewsRequest adventureReviewsRequest) {
        adventureReviewsService.save(adventureReviewsRequest);
    }

    @GetMapping(path = "/adventurereviews/all")
    public List<AdventureReviewResponse> getAllAdventureReviews() {
        return adventureReviewsService.getAllAdventureReviews();
    }

    @GetMapping(path = "/adventurereviews/{id}")
    public AdventureReviewResponse getAdventureReviewById(@PathVariable Long id)
    {
        return adventureReviewsService.findbyid(id);
    }

    @PutMapping(path = "/adventurereviews/update/{id}")
    public void updateAdventureReviews(@RequestBody @Valid AdventureReviewsRequest adventureReviewsRequest,@PathVariable Long id){
        adventureReviewsService.updateById(adventureReviewsRequest,id);
    }

    @DeleteMapping(path = "/adventurereviews/delete/{id}")
    public void deleteAdventureReviewbyId(@PathVariable Long id) {
        adventureReviewsService.deleteById(id);
    }



}
