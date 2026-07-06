package org.example.financeservice.dto.finance;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

@Schema(description = "Payload used to create a payment method.")
@Value
@Builder
public class CreatePaymentMethodRequest {
    @NotBlank
    @Schema(description = "Human-readable payment method name.", example = "Credit Card")
    String methodName;

    @Schema(description = "Whether the payment method can be used for new transactions.", example = "true")
    boolean active;
}
