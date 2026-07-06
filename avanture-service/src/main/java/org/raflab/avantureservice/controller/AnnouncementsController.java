package org.raflab.avantureservice.controller;

import jakarta.validation.Valid;
import org.raflab.avantureservice.dto.request.AdventureCategoriesRequest;
import org.raflab.avantureservice.dto.request.AnnouncementsRequest;
import org.raflab.avantureservice.dto.response.AdventureCategoriesResponse;
import org.raflab.avantureservice.dto.response.AnnouncementsResponse;
import org.raflab.avantureservice.service.AnnouncementsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8083")
@RestController
@RequestMapping("/api/avanture")
@RefreshScope
public class AnnouncementsController {
    @Autowired
    private AnnouncementsService announcementsService;
    @PostMapping(path = "/announcements/add")
    public void addAnnouncement(@RequestBody @Valid AnnouncementsRequest announcementsRequest) {
        announcementsService.save(announcementsRequest);
    }

    @GetMapping(path = "/announcements/all")
    public List<AnnouncementsResponse> getAllAnnouncements() {
        return announcementsService.getAllAnnouncements();
    }

    @GetMapping(path = "/announcements/{id}")
    public AnnouncementsResponse getAnnouncementById(@PathVariable Long id)
    {
        return announcementsService.findbyid(id);
    }

    @PutMapping(path = "/announcements/update/{id}")
    public void updateAnnouncement(@RequestBody @Valid AnnouncementsRequest announcementsRequest,@PathVariable Long id){
        announcementsService.updateById(announcementsRequest,id);
    }


    @DeleteMapping(path = "/announcements/delete/{id}")
    public void deleteAnnouncementbyId(@PathVariable Long id) {
        announcementsService.deleteById(id);
    }
}
