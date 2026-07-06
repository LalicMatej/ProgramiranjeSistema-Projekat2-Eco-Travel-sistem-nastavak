package org.raflab.avantureservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GearRequirementsRequest {
    @NotEmpty(message = "Polje name mora biti uneseno")
    private String item_name;
    @NotNull(message = "Polje is mandatory mora biti uneseno")
    private boolean is_mandatory;
    @NotNull(message = "Polje adventureRequest mora biti uneseno")
    private AdventuresRequest adventuresRequest;

}
