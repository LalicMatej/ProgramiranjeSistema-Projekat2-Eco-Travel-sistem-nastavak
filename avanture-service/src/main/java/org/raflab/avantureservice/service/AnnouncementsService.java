package org.raflab.avantureservice.service;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.raflab.avantureservice.client.VodiciClient;
import org.raflab.avantureservice.converter.AnnouncementsConverter;
import org.raflab.avantureservice.converter.TimeSlotsConverter;
import org.raflab.avantureservice.dto.request.AdventuresRequest;
import org.raflab.avantureservice.dto.request.AnnouncementsRequest;
import org.raflab.avantureservice.dto.response.AnnouncementsResponse;
import org.raflab.avantureservice.dto.response.GuideResponse;
import org.raflab.avantureservice.model.Announcements;
import org.raflab.avantureservice.model.TimeSlots;
import org.raflab.avantureservice.observer.AnnouncementsSubscriber;
import org.raflab.avantureservice.repositories.AnnouncementsRepository;
import org.raflab.avantureservice.repositories.TimeSlotsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnnouncementsService implements AnnouncementsSubscriber {
    @Autowired
    private AnnouncementsRepository announcementsRepository;
    @Autowired
    private TimeSlotsRepository timeSlotsRepository;


    public void save(AnnouncementsRequest announcementsRequest) {
        Announcements announcements = AnnouncementsConverter.toAnnouncement(announcementsRequest);

        Optional<TimeSlots> ts=timeSlotsRepository.findByTermMark(announcementsRequest.getTimeSlotsRequest().getTermMark());
        if(ts.isPresent()){
            announcements.setTimeSlots(ts.get());
        }else {
            TimeSlots newTimeSlots = TimeSlotsConverter.toTimeSlots(announcementsRequest.getTimeSlotsRequest());
            timeSlotsRepository.save(newTimeSlots);
            announcements.setTimeSlots(newTimeSlots);
        }

        announcementsRepository.save(announcements);
    }
    public List<AnnouncementsResponse> getAllAnnouncements() {
        return AnnouncementsConverter.toAnnouncementsResponseList(announcementsRepository.findAll());
    }

    public void updateById(AnnouncementsRequest announcementsRequest,Long id){

        if(announcementsRepository.existsById(id)){
            Announcements announcements=new Announcements();
            announcements.setTitle(announcementsRequest.getTitle());
            announcements.setDescription(announcementsRequest.getDescription());
            announcements.setTimeSlots(TimeSlotsConverter.toTimeSlots(announcementsRequest.getTimeSlotsRequest()));

            announcementsRepository.save(announcements);
        }else {
            announcementsRepository.save(AnnouncementsConverter.toAnnouncement(announcementsRequest));
        }
    }

    public AnnouncementsResponse findbyid(Long id){
        return AnnouncementsConverter.toAnnouncementResponse(announcementsRepository.findById(id).get());
    }

    public void deleteById(Long id){
        announcementsRepository.deleteById(id);
    }

    @Override
    public void update(TimeSlots timeSlots) {
        Announcements announcements=new Announcements();
        announcements.setTitle("Putovanje");
        announcements.setDescription("Tura pocinje:"+timeSlots.getStartTime()+",tura se zavrsava:"+timeSlots.getEndTime());
        announcements.setTimeSlots(timeSlots);

        announcementsRepository.save(announcements);
    }
}
