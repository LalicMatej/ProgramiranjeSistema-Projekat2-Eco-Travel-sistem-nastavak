package org.example.bookingservice.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Schema(description = "Standard error payload returned by the API.")
@Value
@Builder
public class ApiErrorResponse {
    @Schema(description = "Timestamp when the error response was created.", example = "2026-04-07T12:40:00Z")
    Instant timestamp;

    @Schema(description = "HTTP status code.", example = "400")
    int status;

    @Schema(description = "HTTP status reason phrase.", example = "Bad Request")
    String error;

    @Schema(description = "Main error message.", example = "Booking end date must be after start date.")
    String message;

    @Schema(description = "Request path that caused the error.", example = "/api/bookings")
    String path;

    @Schema(description = "Validation errors collected for invalid requests.")
    List<String> validationErrors;
}
