package org.example.bookingservice.dto.guest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

@Schema(description = "Payload used to register a new guest.")
@Value
@Builder
public class CreateGuestRequest {
    @NotBlank
    @Schema(description = "Guest first name.", example = "Ana")
    String firstName;

    @NotBlank
    @Schema(description = "Guest last name.", example = "Markovic")
    String lastName;

    @NotBlank
    @Email
    @Schema(description = "Guest email address. Must be unique.", example = "ana.markovic@raf.rs")
    String email;
}
