package org.example.bookingservice.dto.booking;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Schema(description = "Preview of the currently stored total price for a booking.")
@Value
@Builder
public class PricePreviewResponse {
    @Schema(description = "Booking identifier.", example = "1")
    Long bookingId;

    @Schema(description = "Stored base booking price before add-on items are applied.", example = "475.00")
    BigDecimal basePrice;

    @Schema(description = "Sum of all add-on item prices attached to the booking.", example = "25.00")
    BigDecimal addOnTotal;

    @Schema(description = "Calculated final price: base price plus add-on total.", example = "500.00")
    BigDecimal totalPrice;
}
