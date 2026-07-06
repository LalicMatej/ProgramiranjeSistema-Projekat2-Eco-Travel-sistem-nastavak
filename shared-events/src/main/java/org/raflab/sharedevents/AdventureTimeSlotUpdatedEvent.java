package org.raflab.sharedevents;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdventureTimeSlotUpdatedEvent {
    private Long timeSlotId;
    private Long adventureId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate oldStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate oldEndDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate newStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate newEndDate;
}
