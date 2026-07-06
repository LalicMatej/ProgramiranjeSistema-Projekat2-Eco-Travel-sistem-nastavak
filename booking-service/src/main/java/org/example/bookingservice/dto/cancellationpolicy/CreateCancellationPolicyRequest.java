package org.example.bookingservice.dto.cancellationpolicy;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Schema(description = "Payload used to create a new cancellation policy.")
@Value
@Builder
public class CreateCancellationPolicyRequest {
    @NotBlank
    @Schema(description = "Human-readable policy name.", example = "Flexible")
    String policyName;

    @NotNull
    @DecimalMin("0.0")
    @Schema(description = "Refund percentage paid back to the guest.", example = "100.00")
    BigDecimal refundPercentage;
}
