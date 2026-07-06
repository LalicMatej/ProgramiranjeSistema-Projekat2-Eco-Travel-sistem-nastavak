package org.example.bookingservice.dto.cancellationpolicy;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Schema(description = "Cancellation policy data returned by the booking service.")
@Value
@Builder
public class CancellationPolicyResponse {
    @Schema(description = "Cancellation policy identifier.", example = "1")
    Long id;

    @Schema(description = "Policy name displayed to users.", example = "Flexible")
    String policyName;

    @Schema(description = "Refund percentage allowed by the policy.", example = "100.00")
    BigDecimal refundPercentage;
}
