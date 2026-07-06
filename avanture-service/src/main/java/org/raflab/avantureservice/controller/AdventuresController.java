package org.raflab.avantureservice.controller;

import jakarta.validation.Valid;
import org.raflab.avantureservice.dto.request.AdventureReviewsRequest;
import org.raflab.avantureservice.dto.request.AdventuresRequest;
import org.raflab.avantureservice.dto.response.AdventureReviewResponse;
import org.raflab.avantureservice.dto.response.AdventuresResponse;
import org.raflab.avantureservice.service.AdventureReviewsService;
import org.raflab.avantureservice.service.AdventuresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8083")
@RestController
@RequestMapping("/api/avanture")
@RefreshScope
public class AdventuresController {
    @Autowired
    private AdventuresService  adventuresService;
    @PostMapping(path = "/adventures/add")
    public void addAdventures(@RequestBody @Valid AdventuresRequest adventuresRequest) {
        adventuresService.save(adventuresRequest);
    }

    @GetMapping(path = "/adventures/all")
    public List<AdventuresResponse> getAllAdventures() {
        return adventuresService.getAllAdventures();
    }

    @GetMapping(path = "/adventures/{id}")
    public AdventuresResponse getAdventureById(@PathVariable Long id)
    {
        return adventuresService.findbyid(id);
    }

    @PutMapping(path = "/adventures/update/{id}")
    public void updateAdventure(@RequestBody @Valid AdventuresRequest adventuresRequest,@PathVariable Long id){
        adventuresService.updateById(adventuresRequest,id);
    }

    @DeleteMapping(path = "/adventures/delete/{id}")
    public void deleteAdventureById(@PathVariable Long id) {
        adventuresService.deleteById(id);
    }

    @GetMapping(path = "/adventures/totalprice")
    public Double getAveragePrice()
    {
        return adventuresService.getAveragePriceMethod();
    }



}
