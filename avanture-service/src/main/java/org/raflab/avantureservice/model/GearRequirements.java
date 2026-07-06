package org.raflab.avantureservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class GearRequirements {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String item_name;
    private boolean is_mandatory;
    @ManyToOne
    private Adventures adventures;
}
