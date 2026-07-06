package smestajmikroservis.exception;

import java.time.Instant;

public record ErrorResponse(
        String message,
        int status,
        String traceId,
        String timestamp
) {
    public static ErrorResponse of(String message, int status, String traceId) {
        return new ErrorResponse(message, status, traceId, Instant.now().toString());
    }
}
