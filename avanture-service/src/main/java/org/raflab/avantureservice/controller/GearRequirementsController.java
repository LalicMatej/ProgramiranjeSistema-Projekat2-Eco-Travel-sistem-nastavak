package org.raflab.avantureservice.controller;

import jakarta.validation.Valid;
import org.raflab.avantureservice.dto.request.AdventureReviewsRequest;
import org.raflab.avantureservice.dto.request.AdventuresRequest;
import org.raflab.avantureservice.dto.request.GearRequirementsRequest;
import org.raflab.avantureservice.dto.response.AdventuresResponse;
import org.raflab.avantureservice.dto.response.GearRequirementsResponse;
import org.raflab.avantureservice.service.AdventuresService;
import org.raflab.avantureservice.service.GearRequirementsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8083")
@RestController
@RequestMapping("/api/avanture")
@RefreshScope
public class GearRequirementsController {
    @Autowired
    private GearRequirementsService gearRequirementsService;
    @PostMapping(path = "/gearrequirements/add")
    public void addGearRequirement(@RequestBody @Valid GearRequirementsRequest gearRequirementsRequest) {
        gearRequirementsService.save(gearRequirementsRequest);
    }

    @GetMapping(path = "/gearrequirements/all")
    public List<GearRequirementsResponse> getAllGearRequirements() {
        return gearRequirementsService.getAllGearRequirements();
    }

    @GetMapping(path = "/gearrequirements/{id}")
    public GearRequirementsResponse getGearRequirementById(@PathVariable Long id)
    {
        return gearRequirementsService.findbyid(id);
    }

    @PutMapping(path = "/gearrequirements/update/{id}")
    public void updateGearRequirement(@RequestBody @Valid GearRequirementsRequest gearRequirementsRequest, @PathVariable Long id){
        gearRequirementsService.updateById(gearRequirementsRequest,id);
    }

    @DeleteMapping(path = "/gearrequirements/delete/{id}")
    public void deleteGearRequirementById(@PathVariable Long id) {
        gearRequirementsService.deleteById(id);
    }

}
