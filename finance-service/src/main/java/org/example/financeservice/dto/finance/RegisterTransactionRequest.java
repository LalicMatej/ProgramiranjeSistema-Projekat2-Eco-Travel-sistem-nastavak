package org.example.financeservice.dto.finance;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Schema(description = "Payload used to register a payment transaction for an invoice.")
@Value
@Builder
public class RegisterTransactionRequest {
    @NotNull
    @Schema(description = "Payment method identifier used for the transaction.", example = "1")
    Long paymentMethodId;

    @NotNull
    @DecimalMin("0.0")
    @Schema(description = "Transaction amount to be paid.", example = "570.00")
    BigDecimal amount;
}
