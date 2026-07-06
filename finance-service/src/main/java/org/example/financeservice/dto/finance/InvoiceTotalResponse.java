package org.example.financeservice.dto.finance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Schema(description = "Computed invoice total summary.")
@Value
@Builder
public class InvoiceTotalResponse {
    @Schema(description = "Invoice identifier.", example = "1")
    Long invoiceId;

    @Schema(description = "Subtotal before tax.", example = "475.00")
    BigDecimal subtotal;

    @Schema(description = "Total amount after tax.", example = "570.00")
    BigDecimal totalAmount;
}
