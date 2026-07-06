package org.raflab.avantureservice.dto.request;

import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.raflab.avantureservice.model.Adventures;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
public class TimeSlotsRequest {
    @NotEmpty(message = "Polje termMark mora biti uneseno")
    private String termMark;
    @NotNull(message = "Polje startTime mora biti uneseno")
    private LocalDate startTime;
    @NotNull(message = "Polje endTime mora biti uneseno")
    private LocalDate endTime;
    @NotNull(message = "Polje max_capacity mora biti uneseno")
    private Integer max_capacity;
    @NotNull(message = "Polje currentOccupancy mora biti uneseno")
    private Integer current_occupancy;
    @NotNull(message = "Polje adventureRequest mora biti uneseno")
    private AdventuresRequest adventuresRequest;
}
