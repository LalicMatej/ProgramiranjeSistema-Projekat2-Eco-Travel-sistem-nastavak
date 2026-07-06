package org.raflab.avantureservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
public class AdventureReviewsRequest {
    @NotNull(message = "Polje rating mora biti uneseno")
    private Integer rating;
    private String comment;
    @NotNull(message = "Polje created_at mora biti uneseno")
    private LocalDate created_at;
    @NotNull(message = "Polje adventureRequest mora biti uneseno")
    private AdventuresRequest adventuresRequest;

}
