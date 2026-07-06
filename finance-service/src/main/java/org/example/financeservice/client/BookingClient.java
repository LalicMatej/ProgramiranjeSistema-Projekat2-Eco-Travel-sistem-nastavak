package org.example.financeservice.client;

import org.example.financeservice.dto.integration.RemoteBookingPricePreviewResponse;
import org.example.financeservice.dto.integration.RemoteBookingResponse;
import org.example.financeservice.dto.integration.RemoteBookingSummaryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "bookingClient", url = "${booking-service.base-url}")
public interface BookingClient {

    @GetMapping("/api/bookings/{id}")
    RemoteBookingResponse getBookingById(@PathVariable("id") Long id);

    @GetMapping("/api/bookings/{id}/price-preview")
    RemoteBookingPricePreviewResponse getBookingPricePreview(@PathVariable("id") Long id);

    @GetMapping("/api/bookings/{id}/summary")
    RemoteBookingSummaryResponse getBookingSummary(@PathVariable("id") Long id);
}
