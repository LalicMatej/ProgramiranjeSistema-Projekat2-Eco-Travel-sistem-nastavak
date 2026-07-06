package org.raflab.avantureservice.dto.response;

import lombok.Data;

@Data
public class AdventuresResponse {
    private Long id;
    private String title;
    private String description;
    private String difficulty_level;
    private Double base_price;
    private Integer guideLevelRequired;
    private Long guide_id;
    private AdventureCategoriesResponse adventureCategoriesResponse;
}
