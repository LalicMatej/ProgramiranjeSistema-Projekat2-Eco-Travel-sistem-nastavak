package org.raflab.avantureservice.converter;

import org.raflab.avantureservice.dto.request.AdventuresRequest;
import org.raflab.avantureservice.dto.request.AnnouncementsRequest;
import org.raflab.avantureservice.dto.response.AdventuresResponse;
import org.raflab.avantureservice.dto.response.AnnouncementsResponse;
import org.raflab.avantureservice.model.Adventures;
import org.raflab.avantureservice.model.Announcements;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementsConverter {
    public static Announcements toAnnouncement(AnnouncementsRequest announcementsRequest) {
        Announcements announcements = new Announcements();
        announcements.setTitle(announcementsRequest.getTitle());
        announcements.setDescription(announcementsRequest.getDescription());

        return announcements;
    }
    public static AnnouncementsResponse toAnnouncementResponse(Announcements announcements) {
        AnnouncementsResponse announcementsResponse = new AnnouncementsResponse();
        announcementsResponse.setId(announcements.getId());
        announcementsResponse.setTitle(announcements.getTitle());
        announcementsResponse.setDescription(announcements.getDescription());
        announcementsResponse.setTimeSlotsResponse(TimeSlotsConverter.toTimeSlotResponse(announcements.getTimeSlots()));

        return announcementsResponse;

    }

    public static List<AnnouncementsResponse> toAnnouncementsResponseList(List<Announcements> announcementsList){
        List<AnnouncementsResponse> announcementsResponseList = new ArrayList<>();
        for(Announcements an:announcementsList){
            announcementsResponseList.add(toAnnouncementResponse(an));
        }
        return announcementsResponseList;
    }
}
