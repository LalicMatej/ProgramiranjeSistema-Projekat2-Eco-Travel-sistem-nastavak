package org.raflab.avantureservice.controller;

import jakarta.validation.Valid;
import org.raflab.avantureservice.dto.request.AdventureReviewsRequest;
import org.raflab.avantureservice.dto.request.GearRequirementsRequest;
import org.raflab.avantureservice.dto.request.TimeSlotsRequest;
import org.raflab.avantureservice.dto.response.GearRequirementsResponse;
import org.raflab.avantureservice.dto.response.TimeSlotsResponse;
import org.raflab.avantureservice.service.GearRequirementsService;
import org.raflab.avantureservice.service.TimeSlotsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8083")
@RestController
@RequestMapping("/api/avanture")
@RefreshScope
public class TimeSlotsController {
    @Autowired
    private TimeSlotsService timeSlotsService;
    @PostMapping(path = "/timeslots/add")
    public void addTimeSlots(@RequestBody @Valid TimeSlotsRequest timeSlotsRequest) {
        timeSlotsService.save(timeSlotsRequest);
    }

    @GetMapping(path = "/timeslots/all")
    public List<TimeSlotsResponse> getAllTimeSlots() {
        return timeSlotsService.getAllTimeSlots();
    }

    @GetMapping(path = "/timeslots/{id}")
    public TimeSlotsResponse getTimeSlotById(@PathVariable Long id)
    {
        return timeSlotsService.findbyid(id);
    }

    @PutMapping(path = "/timeslots/update/{id}")
    public void updateTimeSlot(@RequestBody @Valid TimeSlotsRequest timeSlotsRequest, @PathVariable Long id){
        timeSlotsService.updateById(timeSlotsRequest,id);
    }

    @DeleteMapping(path = "/timeslots/delete/{id}")
    public void deleteTimeSlotById(@PathVariable Long id) {
        timeSlotsService.deleteById(id);
    }

}
