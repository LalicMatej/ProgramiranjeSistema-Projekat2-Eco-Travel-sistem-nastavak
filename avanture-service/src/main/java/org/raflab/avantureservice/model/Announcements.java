package org.raflab.avantureservice.model;

import jakarta.persistence.*;
import lombok.Data;
import org.raflab.avantureservice.dto.request.TimeSlotsRequest;

@Data
@Entity
public class Announcements {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @ManyToOne
    private TimeSlots timeSlots;

}
