package org.raflab.vodiciservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LanguagesRequest {
    @NotEmpty(message = "Polje language_name mora biti uneseno")
    private String language_name;
    @NotEmpty(message = "Polje language_code mora biti uneseno")
    private String language_code;
}
