package org.raflab.vodiciservice.dto.response;

import lombok.Data;
import org.raflab.vodiciservice.dto.request.GuideRequest;

import java.time.LocalDate;

@Data
public class VacationsResponse {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String destination;
    private GuideResponse guideResponse;
}
