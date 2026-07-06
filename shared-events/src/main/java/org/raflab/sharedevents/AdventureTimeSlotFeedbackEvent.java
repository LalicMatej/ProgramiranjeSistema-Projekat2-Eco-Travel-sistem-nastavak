package org.raflab.sharedevents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdventureTimeSlotFeedbackEvent {
    private Long timeSlotId;
    private String status;
    private String message;
}
