package odrzavanjemikroservis.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)  // ← DODAJ OVO!

public class AvailabilityCalendarDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private String unitName;
    private String unitType;
}
