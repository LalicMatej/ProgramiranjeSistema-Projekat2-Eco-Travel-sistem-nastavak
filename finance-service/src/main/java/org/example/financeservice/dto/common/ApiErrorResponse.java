package org.example.financeservice.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Schema(description = "Standard error payload returned by the finance service.")
@Value
@Builder
public class ApiErrorResponse {
    @Schema(description = "Timestamp when the error was generated.", example = "2026-04-07T12:40:00Z")
    Instant timestamp;

    @Schema(description = "HTTP status code.", example = "404")
    int status;

    @Schema(description = "HTTP status reason phrase.", example = "Not Found")
    String error;

    @Schema(description = "Main error message.", example = "Invoice with id 5 was not found.")
    String message;

    @Schema(description = "Request path that failed.", example = "/api/invoices/5")
    String path;

    @Schema(description = "Validation error details if the request body was invalid.")
    List<String> validationErrors;
}
