package org.raflab.sharedevents;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrderCreatedEvent {
    private Long workOrderId;
    private Long unitId;
    private Long workerId;
    private Long taskId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime scheduledFor;
    private String taskName;
    private Integer estimatedDurationHours;
}

