package org.example.bookingservice.dto.booking;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;
import org.example.bookingservice.entity.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Booking row returned by the complex booking search query.")
@Value
public class BookingSearchResponse {
    @Schema(description = "Booking identifier.", example = "1")
    Long bookingId;

    @Schema(description = "Reserved accommodation unit identifier.", example = "101")
    Long unitId;

    @Schema(description = "Guest full name.", example = "Ana Markovic")
    String guestName;

    @Schema(description = "Guest email used for filtering.", example = "ana.markovic@raf.rs")
    String guestEmail;

    @Schema(description = "Applied cancellation policy name.", example = "Flexible", nullable = true)
    String policyName;

    @Schema(description = "Booking start date.", example = "2026-05-10")
    LocalDate startDate;

    @Schema(description = "Booking end date.", example = "2026-05-15")
    LocalDate endDate;

    @Schema(description = "Stored booking price.", example = "475.00")
    BigDecimal totalPrice;

    @Schema(description = "Booking status.", example = "PENDING")
    BookingStatus status;
}
