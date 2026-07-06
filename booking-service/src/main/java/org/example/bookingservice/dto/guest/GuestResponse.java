package org.example.bookingservice.dto.guest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Schema(description = "Guest data returned by the booking service.")
@Value
@Builder
public class GuestResponse {
    @Schema(description = "Guest identifier.", example = "1")
    Long id;

    @Schema(description = "Guest first name.", example = "Ana")
    String firstName;

    @Schema(description = "Guest last name.", example = "Markovic")
    String lastName;

    @Schema(description = "Guest email address.", example = "ana.markovic@raf.rs")
    String email;
}
