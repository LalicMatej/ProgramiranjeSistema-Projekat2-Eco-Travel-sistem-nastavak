package org.raflab.vodiciservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Languages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String language_name;
    private String language_code;
    @ManyToMany(mappedBy = "languages")
    private List<Guides> guides;
}
