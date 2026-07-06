package org.raflab.vodiciservice.controller;

import jakarta.validation.Valid;
import org.raflab.vodiciservice.dto.request.CertificationsRequest;
import org.raflab.vodiciservice.dto.request.GuideRequest;
import org.raflab.vodiciservice.dto.response.GuideResponse;
import org.raflab.vodiciservice.service.GuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8083")
@RestController
@RequestMapping("/api/vodici")
@RefreshScope
public class GuideController {
    @Autowired
    private GuideService guideService;
    @PostMapping(path = "/guide/add")
    public void addGuide(@RequestBody @Valid GuideRequest guideRequest) {
        guideService.save(guideRequest);
    }

    @GetMapping(path = "/guide/all")
    public List<GuideResponse> getAllGuides() {
        return guideService.getAllGuides();
    }

    @GetMapping(path = "/guide/guideid/{id}")
    public GuideResponse getGuideById(@PathVariable Long id)
    {
        return guideService.findbyid(id);
    }

    @PutMapping(path = "/guide/update/{id}")
    public void updateGuide(@RequestBody @Valid GuideRequest guideRequest, @PathVariable Long id){
        guideService.updateById(guideRequest,id);
    }

    @GetMapping(path = "/guide/averagerating")
    public Double getAverageRating()
    {
        return guideService.getAverageRatingMethod();
    }

    @DeleteMapping(path = "/guide/delete/{id}")
    public void deleteTimeSlotById(@PathVariable Long id) {
        guideService.deleteById(id);
    }
}
