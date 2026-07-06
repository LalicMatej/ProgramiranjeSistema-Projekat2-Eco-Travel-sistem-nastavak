package org.example.financeservice.dto.finance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Schema(description = "Tax rate data returned by the finance service.")
@Value
@Builder
public class TaxRateResponse {
    @Schema(description = "Tax rate identifier.", example = "1")
    Long id;

    @Schema(description = "Tax rate name.", example = "VAT")
    String taxName;

    @Schema(description = "Tax percentage value.", example = "20.00")
    BigDecimal percentage;
}
