package org.raflab.vodiciservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class VacationsRequest {
    @NotNull(message = "Polje startDate mora biti uneseno")
    private LocalDate startDate;
    @NotNull(message = "Polje endDate mora biti uneseno")
    private LocalDate endDate;
    @NotEmpty(message = "Polje destination mora biti uneseno")
    private String destination;
    @NotNull(message = "Polje guideRequest mora biti uneseno")
    private GuideRequest guideRequest;
}
