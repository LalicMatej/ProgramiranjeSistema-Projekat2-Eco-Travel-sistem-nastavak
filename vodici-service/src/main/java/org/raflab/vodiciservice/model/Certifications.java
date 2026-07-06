package org.raflab.vodiciservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Certifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String issuing_body;
    private LocalDate expiry_date;
    private Integer certificationLevel;
    @ManyToOne
    private Guides guides;
}
