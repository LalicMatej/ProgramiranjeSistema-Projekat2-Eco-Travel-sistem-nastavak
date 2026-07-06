package org.raflab.avantureservice.dto.response;

import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.raflab.avantureservice.model.TimeSlots;

@Data
public class AnnouncementsResponse {
    private Long id;
    private String title;
    private String description;
    private TimeSlotsResponse timeSlotsResponse;
}
