package org.raflab.avantureservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdventureCategoriesRequest {
    @NotEmpty(message = "Polje name mora biti uneseno")
    @Schema(example = "Planinarenje")
    private String name;
    @NotEmpty(message = "Polje description mora biti uneseno")
    @Schema(example = "Velik uspon")
    private String description;
}
