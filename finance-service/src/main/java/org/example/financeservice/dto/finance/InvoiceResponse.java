package org.example.financeservice.dto.finance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import org.example.financeservice.entity.enums.InvoiceStatus;

import java.math.BigDecimal;

@Schema(description = "Invoice data returned by the finance service.")
@Value
@Builder
public class InvoiceResponse {
    @Schema(description = "Invoice identifier.", example = "1")
    Long id;

    @Schema(description = "Booking identifier from the booking service.", example = "1")
    Long bookingId;

    @Schema(description = "Applied tax rate identifier.", example = "1")
    Long taxRateId;

    @Schema(description = "Invoice subtotal before tax.", example = "475.00")
    BigDecimal subtotal;

    @Schema(description = "Final invoice total after tax.", example = "570.00")
    BigDecimal totalAmount;

    @Schema(description = "Current invoice status.", example = "PAID")
    InvoiceStatus status;
}
