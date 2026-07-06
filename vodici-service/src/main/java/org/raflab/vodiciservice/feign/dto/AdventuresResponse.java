package org.raflab.vodiciservice.feign.dto;

public record AdventuresResponse(
        Long id,
        String title,
        String description,
        String difficulty_level,
        Double base_price,
        Integer guideLevelRequired,
        Long guide_id
) {}
