package org.example.financeservice.integration;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.financeservice.client.BookingClient;
import org.example.financeservice.dto.integration.RemoteBookingPricePreviewResponse;
import org.example.financeservice.dto.integration.RemoteBookingResponse;
import org.example.financeservice.dto.integration.RemoteBookingSummaryResponse;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingClientFacade {

    public static final String BOOKING_SERVICE_CIRCUIT_BREAKER = "bookingServiceCircuitBreaker";
    public static final String BOOKING_SERVICE_RETRY = "bookingServiceRetry";

    private final BookingClient bookingClient;
    private final BookingClientResultFactory bookingReferenceFactory;

    @Retry(name = BOOKING_SERVICE_RETRY, fallbackMethod = "fallbackBookingDetails")
    @CircuitBreaker(name = BOOKING_SERVICE_CIRCUIT_BREAKER, fallbackMethod = "fallbackBookingDetails")
    public BookingClientResult<RemoteBookingResponse> getBooking(Long bookingId) {
        log.info("Calling booking-service for booking details. bookingId={}", bookingId);
        try {
            RemoteBookingResponse response = bookingClient.getBookingById(bookingId);
            log.info("booking-service returned booking details. bookingId={} status={}", bookingId, response.getStatus());
            return bookingReferenceFactory.success(bookingId, response);
        } catch (FeignException.NotFound exception) {
            log.warn("booking-service returned 404 for booking details. bookingId={}", bookingId);
            return bookingReferenceFactory.notFound(bookingId);
        }
    }

    @Retry(name = BOOKING_SERVICE_RETRY, fallbackMethod = "fallbackBookingPricePreview")
    @CircuitBreaker(name = BOOKING_SERVICE_CIRCUIT_BREAKER, fallbackMethod = "fallbackBookingPricePreview")
    public BookingClientResult<RemoteBookingPricePreviewResponse> getBookingPricePreview(Long bookingId) {
        log.info("Calling booking-service for booking price preview. bookingId={}", bookingId);
        try {
            RemoteBookingPricePreviewResponse response = bookingClient.getBookingPricePreview(bookingId);
            log.info("booking-service returned booking price preview. bookingId={} totalPrice={}",
                    bookingId,
                    response.getTotalPrice());
            return bookingReferenceFactory.success(bookingId, response);
        } catch (FeignException.NotFound exception) {
            log.warn("booking-service returned 404 for booking price preview. bookingId={}", bookingId);
            return bookingReferenceFactory.notFound(bookingId);
        }
    }

    @Retry(name = BOOKING_SERVICE_RETRY, fallbackMethod = "fallbackBookingSummary")
    @CircuitBreaker(name = BOOKING_SERVICE_CIRCUIT_BREAKER, fallbackMethod = "fallbackBookingSummary")
    public BookingClientResult<RemoteBookingSummaryResponse> getBookingSummary(Long bookingId) {
        log.info("Calling booking-service for booking summary. bookingId={}", bookingId);
        try {
            RemoteBookingSummaryResponse response = bookingClient.getBookingSummary(bookingId);
            log.info("booking-service returned booking summary. bookingId={} status={}", bookingId, response.getStatus());
            return bookingReferenceFactory.success(bookingId, response);
        } catch (FeignException.NotFound exception) {
            log.warn("booking-service returned 404 for booking summary. bookingId={}", bookingId);
            return bookingReferenceFactory.notFound(bookingId);
        }
    }

    private BookingClientResult<RemoteBookingResponse> fallbackBookingDetails(Long bookingId, Throwable throwable) {
        log.warn("Fallback triggered for booking details lookup. bookingId={}", bookingId, throwable);
        return bookingReferenceFactory.serviceUnavailable(bookingId, throwable);
    }

    private BookingClientResult<RemoteBookingPricePreviewResponse> fallbackBookingPricePreview(Long bookingId,
                                                                                               Throwable throwable) {
        log.warn("Fallback triggered for booking price preview lookup. bookingId={}", bookingId, throwable);
        return bookingReferenceFactory.serviceUnavailable(bookingId, throwable);
    }

    private BookingClientResult<RemoteBookingSummaryResponse> fallbackBookingSummary(Long bookingId, Throwable throwable) {
        log.warn("Fallback triggered for booking summary lookup. bookingId={}", bookingId, throwable);
        return bookingReferenceFactory.serviceUnavailable(bookingId, throwable);
    }
}
