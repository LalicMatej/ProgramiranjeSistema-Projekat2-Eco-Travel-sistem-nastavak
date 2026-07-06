package org.raflab.vodiciservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GuideRequest {
    @NotEmpty(message = "Polje first_name mora biti uneseno")
    private String first_name;
    @NotEmpty(message = "Polje last_name mora biti uneseno")
    private String last_name;
    @NotEmpty(message = "Polje bio mora biti uneseno")
    private String bio;
    @NotNull(message = "Polje rating mora biti uneseno")
    private Double rating;
}
