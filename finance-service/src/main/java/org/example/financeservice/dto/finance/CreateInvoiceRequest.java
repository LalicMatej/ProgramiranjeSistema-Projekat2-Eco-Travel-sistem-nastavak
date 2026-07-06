package org.example.financeservice.dto.finance;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import org.example.financeservice.entity.enums.InvoiceStatus;

import java.math.BigDecimal;

@Schema(description = "Payload used to create a new invoice.")
@Value
@Builder
public class CreateInvoiceRequest {
    @NotNull
    @Schema(description = "Booking identifier from the booking service.", example = "1")
    Long bookingId;

    @NotNull
    @Schema(description = "Tax rate identifier applied to the invoice.", example = "1")
    Long taxRateId;

    @NotNull
    @DecimalMin("0.0")
    @Schema(description = "Invoice amount before tax.", example = "475.00")
    BigDecimal subtotal;

    @NotNull
    @Schema(description = "Initial invoice status.", example = "ISSUED")
    InvoiceStatus status;
}
