package org.example.bookingservice.dto.integration;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Kratak pregled aktivnih rezervacija za smeštajnu jedinicu.")
public record UnitBookingSummaryResponse(
        @Schema(description = "Identifikator smeštajne jedinice.", example = "1")
        Long unitId,

        @Schema(description = "Broj aktivnih rezervacija za jedinicu.", example = "2")
        long activeBookingCount,

        @Schema(description = "Da li jedinica ima aktivne rezervacije.", example = "true")
        boolean hasActiveBookings
) {
}
