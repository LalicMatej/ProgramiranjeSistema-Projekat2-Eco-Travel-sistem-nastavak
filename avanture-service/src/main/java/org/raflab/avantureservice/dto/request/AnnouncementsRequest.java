package org.raflab.avantureservice.dto.request;

import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.raflab.avantureservice.model.TimeSlots;

@Data
public class AnnouncementsRequest {
    @NotEmpty(message = "Polje title mora biti uneseno")
    private String title;
    @NotEmpty(message = "Polje description mora biti uneseno")
    private String description;
    @NotNull(message = "Polje timeSlotsRequest mora biti uneseno")
    private TimeSlotsRequest timeSlotsRequest;
}
