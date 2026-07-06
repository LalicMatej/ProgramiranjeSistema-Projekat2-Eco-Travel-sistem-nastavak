package org.raflab.avantureservice.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VacationsResponse {
    private LocalDate startDate;
    private LocalDate endDate;
}
