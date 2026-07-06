package org.example.financeservice.dto.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Schema(description = "Basic finance service status response.")
@Value
@Builder
public class SystemStatusResponse {
    @Schema(description = "Application name.", example = "finance-service")
    String serviceName;

    @Schema(description = "Current API version label.", example = "v1-zadatak1")
    String version;

    @Schema(description = "Timestamp of the status response.", example = "2026-04-07T12:42:16.820Z")
    Instant timestamp;

    @Schema(description = "Service health indicator.", example = "UP")
    String status;
}
