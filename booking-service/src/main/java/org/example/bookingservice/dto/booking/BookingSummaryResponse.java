package org.example.bookingservice.dto.booking;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import org.example.bookingservice.entity.enums.BookingStatus;

import java.time.LocalDate;

@Schema(description = "Compact booking payload intended for internal service-to-service communication.")
@Value
@Builder
public class BookingSummaryResponse {
    @Schema(description = "Booking identifier.", example = "1")
    Long id;

    @Schema(description = "Guest identifier.", example = "1")
    Long guestId;

    @Schema(description = "Accommodation unit identifier.", example = "101")
    Long unitId;

    @Schema(description = "Arrival date.", example = "2026-05-10")
    LocalDate startDate;

    @Schema(description = "Departure date.", example = "2026-05-15")
    LocalDate endDate;

    @Schema(description = "Current booking status.", example = "CONFIRMED")
    BookingStatus status;
}
