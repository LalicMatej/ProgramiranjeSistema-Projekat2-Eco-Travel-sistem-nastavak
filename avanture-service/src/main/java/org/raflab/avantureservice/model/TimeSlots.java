package org.raflab.avantureservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Data
public class TimeSlots {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String termMark;
    private LocalDate startTime;
    private LocalDate endTime;
    private Integer max_capacity;
    private Integer current_occupancy;
    private String feedbackStatus;
    private String feedbackMessage;
    @ManyToOne
    private Adventures adventures;
}
