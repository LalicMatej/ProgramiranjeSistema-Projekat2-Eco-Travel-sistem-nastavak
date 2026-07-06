package org.raflab.avantureservice.model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

@Entity
@Data
public class Adventures {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String difficulty_level;
    private Double base_price;
    private Integer guideLevelRequired;
    @ManyToOne
    private AdventureCategories adventure_categories;
    private Long guide_id;

}
