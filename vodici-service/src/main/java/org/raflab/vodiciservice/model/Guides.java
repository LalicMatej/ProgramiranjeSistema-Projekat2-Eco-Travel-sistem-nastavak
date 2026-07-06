package org.raflab.vodiciservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Guides {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String first_name;
    private String last_name;
    private String bio;
    private Double rating;
    private Integer ratingCount;
    @ManyToMany
    private List<Languages> languages;
}
