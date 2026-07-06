package org.example.bookingservice.dto.booking;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;
import org.example.bookingservice.entity.enums.BookingStatus;

import java.math.BigDecimal;

@Schema(description = "Aggregated booking revenue summary grouped by booking status.")
@Value
public class BookingRevenueSummaryResponse {
    @Schema(description = "Booking status group.", example = "PENDING")
    BookingStatus status;

    @Schema(description = "Number of bookings in the status group.", example = "3")
    Long bookingCount;

    @Schema(description = "Sum of booking total prices without add-ons.", example = "1425.00")
    BigDecimal bookingTotalPrice;

    @Schema(description = "Sum of all add-on item prices for the status group.", example = "75.00")
    BigDecimal addOnTotalPrice;

    @Schema(description = "Combined revenue: booking total price plus add-on total price.", example = "1500.00")
    BigDecimal combinedRevenue;
}
