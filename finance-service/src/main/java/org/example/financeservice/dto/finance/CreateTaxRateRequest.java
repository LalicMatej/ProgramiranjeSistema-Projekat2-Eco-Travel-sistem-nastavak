package org.example.financeservice.dto.finance;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Schema(description = "Payload used to create a tax rate.")
@Value
@Builder
public class CreateTaxRateRequest {
    @NotBlank
    @Schema(description = "Unique tax rate name.", example = "VAT")
    String taxName;

    @NotNull
    @DecimalMin("0.0")
    @Schema(description = "Tax rate percentage value.", example = "20.00")
    BigDecimal percentage;
}
