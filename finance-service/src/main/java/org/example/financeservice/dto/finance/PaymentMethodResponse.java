package org.example.financeservice.dto.finance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Schema(description = "Payment method data returned by the finance service.")
@Value
@Builder
public class PaymentMethodResponse {
    @Schema(description = "Payment method identifier.", example = "1")
    Long id;

    @Schema(description = "Payment method name.", example = "Credit Card")
    String methodName;

    @Schema(description = "Whether the method is active.", example = "true")
    boolean active;
}
