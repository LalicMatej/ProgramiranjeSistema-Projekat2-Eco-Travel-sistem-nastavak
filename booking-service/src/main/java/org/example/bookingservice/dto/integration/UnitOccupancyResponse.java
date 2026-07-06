package org.example.bookingservice.dto.integration;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.example.bookingservice.entity.enums.BookingStatus;

import java.time.LocalDate;

@Builder
@Schema(description = "Status zauzetosti jedinice za konkretan datum.")
public record UnitOccupancyResponse(
        @Schema(description = "Identifikator smeštajne jedinice.", example = "1")
        Long unitId,

        @Schema(description = "Datum za koji se proverava zauzetost.", example = "2026-07-12")
        LocalDate date,

        @Schema(description = "Da li je jedinica zauzeta kroz rezervacioni servis.", example = "true")
        boolean occupied,

        @Schema(description = "Identifikator rezervacije koja pokriva datum.", example = "101")
        Long bookingId,

        @Schema(description = "Status rezervacije.", example = "CONFIRMED")
        BookingStatus bookingStatus
) {
}
