package org.raflab.avantureservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Data
public class AdventureReviews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer rating;
    private String comment;
    private LocalDate created_at;
    private String feedbackStatus;
    private String feedbackMessage;
    @ManyToOne
    private Adventures adventures;
}
