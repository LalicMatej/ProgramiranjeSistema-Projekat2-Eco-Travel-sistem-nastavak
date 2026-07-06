package org.example.bookingservice.dto.booking;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Payload used to create a new booking.")
@Value
@Builder
public class CreateBookingRequest {
    @NotNull
    @Schema(description = "Existing guest identifier.", example = "1")
    Long guestId;

    @NotNull
    @Schema(description = "Accommodation unit identifier.", example = "101")
    Long unitId;

    @Schema(description = "Optional cancellation policy identifier.", example = "1", nullable = true)
    Long policyId;

    @NotNull
    @Future
    @Schema(description = "Booking start date. Must be in the future.", example = "2026-05-10")
    LocalDate startDate;

    @NotNull
    @Future
    @Schema(description = "Booking end date. Must be after the start date.", example = "2026-05-15")
    LocalDate endDate;

    @NotNull
    @DecimalMin("0.0")
    @Schema(description = "Total price that will be assigned to the booking.", example = "475.00")
    BigDecimal totalPrice;
}
