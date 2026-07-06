package org.example.financeservice.integration;

import org.springframework.stereotype.Component;

@Component
public class BookingClientResultFactory {

    public <T> BookingClientResult<T> success(Long bookingId, T payload) {
        return BookingClientResult.<T>builder()
                .status(IntegrationResultStatus.SUCCESS)
                .bookingId(bookingId)
                .payload(payload)
                .message("Booking service call completed successfully.")
                .build();
    }

    public <T> BookingClientResult<T> notFound(Long bookingId) {
        return BookingClientResult.<T>builder()
                .status(IntegrationResultStatus.NOT_FOUND)
                .bookingId(bookingId)
                .message("Booking with id " + bookingId + " was not found in booking-service.")
                .build();
    }

    public <T> BookingClientResult<T> serviceUnavailable(Long bookingId, Throwable throwable) {
        return BookingClientResult.<T>builder()
                .status(IntegrationResultStatus.SERVICE_UNAVAILABLE)
                .bookingId(bookingId)
                .message("Booking service is currently unavailable: " + throwable.getMessage())
                .build();
    }

    public <T> BookingClientResult<T> invalid(Long bookingId, String message) {
        return BookingClientResult.<T>builder()
                .status(IntegrationResultStatus.INVALID_RESPONSE)
                .bookingId(bookingId)
                .message(message)
                .build();
    }
}
