package smestajmikroservis.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class AvailabilityCalendarDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private String unitName;
    private String unitType;
    private Long timeSlotId;
}
