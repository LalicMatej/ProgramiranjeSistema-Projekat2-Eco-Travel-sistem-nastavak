package org.raflab.avantureservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import org.raflab.avantureservice.model.AdventureCategories;

@Data
public class AdventuresRequest {
    @NotEmpty(message = "Polje title mora biti uneseno")
    private String title;
    private String description;
    private String difficulty_level;
    @NotNull(message = "Polje base_price mora biti uneseno")
    private Double base_price;
    @NotNull(message = "Polje adventureCategoriesRequest mora biti uneseno")
    private AdventureCategoriesRequest adventure_categoriesRequest;
    @NotNull(message = "Polje guide_id mora biti uneseno")
    private Long guide_id;
    @NotNull(message = "Polje guideLevelRequired mora biti uneseno")
    private Integer guideLevelRequired;
}
