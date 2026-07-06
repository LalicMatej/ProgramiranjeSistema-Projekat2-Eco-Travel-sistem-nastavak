package org.raflab.vodiciservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Data
public class Vacations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String destination;
    @ManyToOne
    private Guides guides;
}
