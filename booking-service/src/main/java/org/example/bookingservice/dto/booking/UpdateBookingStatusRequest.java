package org.example.bookingservice.dto.booking;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import org.example.bookingservice.entity.enums.BookingStatus;

@Schema(description = "Payload used to update the current booking status.")
@Value
@Builder
public class UpdateBookingStatusRequest {
    @NotNull
    @Schema(description = "New booking status value.", example = "CONFIRMED")
    BookingStatus status;

    @Schema(description = "Optional note explaining the status change.", example = "Guest confirmed arrival by phone.", nullable = true)
    String note;
}
