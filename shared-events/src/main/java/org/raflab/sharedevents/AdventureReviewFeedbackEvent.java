package org.raflab.sharedevents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdventureReviewFeedbackEvent {
    private Long reviewId;
    private Long guideId;
    private String status;
    private String message;
}
