package org.example.bookingservice.dto.booking;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import org.example.bookingservice.entity.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Reservation data returned by the booking service.")
@Value
@Builder
public class BookingResponse {
    @Schema(description = "Unique booking identifier.", example = "1")
    Long id;

    @Schema(description = "Identifier of the guest that owns the booking.", example = "1")
    Long guestId;

    @Schema(description = "Identifier of the reserved accommodation unit.", example = "101")
    Long unitId;

    @Schema(description = "Cancellation policy identifier applied to the booking.", example = "1", nullable = true)
    Long policyId;

    @Schema(description = "Arrival date of the reservation.", example = "2026-05-10")
    LocalDate startDate;

    @Schema(description = "Departure date of the reservation.", example = "2026-05-15")
    LocalDate endDate;

    @Schema(description = "Total booking price stored for the reservation.", example = "475.00")
    BigDecimal totalPrice;

    @Schema(description = "Current booking lifecycle status.", example = "PENDING")
    BookingStatus status;
}
