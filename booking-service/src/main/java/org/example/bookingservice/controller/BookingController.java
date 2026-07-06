package org.example.bookingservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookingservice.dto.booking.BookingResponse;
import org.example.bookingservice.dto.booking.BookingRevenueSummaryResponse;
import org.example.bookingservice.dto.booking.BookingSearchResponse;
import org.example.bookingservice.dto.booking.BookingSummaryResponse;
import org.example.bookingservice.dto.booking.CreateBookingRequest;
import org.example.bookingservice.dto.booking.PricePreviewResponse;
import org.example.bookingservice.dto.booking.UpdateBookingStatusRequest;
import org.example.bookingservice.dto.integration.UnitBookingSummaryResponse;
import org.example.bookingservice.dto.integration.UnitOccupancyResponse;
import org.example.bookingservice.entity.enums.BookingStatus;
import org.example.bookingservice.service.BookingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Tag(name = "Bookings", description = "Endpoints for managing reservations and booking status changes.")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create booking", description = "Creates a new booking for an existing guest and optional cancellation policy.")
    public void createBooking(@Valid @RequestBody CreateBookingRequest request) {
        bookingService.createBooking(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get booking by id", description = "Returns a single booking by its identifier.")
    public BookingResponse getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id);
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search bookings with complex filters",
            description = "Runs a JOIN query over bookings, guests and cancellation policies with optional status, guest email and date filters."
    )
    public List<BookingSearchResponse> searchBookings(
            @RequestParam(required = false) BookingStatus status,
            @RequestParam(required = false) String guestEmail,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endTo) {
        return bookingService.searchBookings(status, guestEmail, startFrom, endTo);
    }

    @GetMapping("/revenue-summary")
    @Operation(
            summary = "Get booking revenue summary",
            description = "Runs an aggregation query grouped by booking status and combines booking prices with add-on item prices."
    )
    public List<BookingRevenueSummaryResponse> getRevenueSummaryByStatus() {
        return bookingService.getRevenueSummaryByStatus();
    }

    @GetMapping("/{id}/summary")
    @Operation(summary = "Get booking summary", description = "Returns a compact booking DTO intended for internal microservice communication.")
    public BookingSummaryResponse getBookingSummary(@PathVariable Long id) {
        return bookingService.getBookingSummary(id);
    }

    @PutMapping("/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update booking status", description = "Changes the current booking status and writes a status log entry.")
    public void updateBookingStatus(@PathVariable Long id,
                                    @Valid @RequestBody UpdateBookingStatusRequest request) {
        bookingService.updateStatus(id, request);
    }

    @GetMapping("/{id}/price-preview")
    @Operation(summary = "Get booking price preview", description = "Returns the currently stored total price for a booking.")
    public PricePreviewResponse getPricePreview(@PathVariable Long id) {
        return bookingService.getPricePreview(id);
    }

    @GetMapping("/internal/units/{unitId}/summary")
    @Operation(summary = "Get unit booking summary", description = "Internal endpoint used by accommodation-service for checking active bookings tied to a unit.")
    public UnitBookingSummaryResponse getUnitBookingSummary(@PathVariable Long unitId) {
        return bookingService.getUnitBookingSummary(unitId);
    }

    @GetMapping("/internal/units/{unitId}/occupancy")
    @Operation(summary = "Get unit occupancy status", description = "Internal endpoint used by accommodation-service for checking whether a unit is occupied on a given date.")
    public UnitOccupancyResponse getUnitOccupancy(@PathVariable Long unitId,
                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return bookingService.getUnitOccupancy(unitId, date);
    }
}
