package org.raflab.avantureservice.dto.response;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
public class AdventureReviewResponse {
    private Long id;
    private Integer rating;
    private String comment;
    private LocalDate created_at;
    private String feedbackStatus;
    private String feedbackMessage;
    private AdventuresResponse adventuresResponse;
}
