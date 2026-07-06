package org.raflab.avantureservice.dto.response;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
public class TimeSlotsResponse {
    private Long id;
    private String termMark;
    private LocalDate startTime;
    private LocalDate endTime;
    private Integer max_capacity;
    private Integer current_occupancy;
    private String feedbackStatus;
    private String feedbackMessage;
    private AdventuresResponse adventuresResponse;
}
