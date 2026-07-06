package org.example.bookingservice.mapper;

import org.example.bookingservice.dto.booking.BookingResponse;
import org.example.bookingservice.dto.booking.BookingSummaryResponse;
import org.example.bookingservice.entity.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public BookingResponse toResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .guestId(booking.getGuest() != null ? booking.getGuest().getId() : null)
                .unitId(booking.getUnitId())
                .policyId(booking.getPolicy() != null ? booking.getPolicy().getId() : null)
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus())
                .build();
    }

    public BookingSummaryResponse toSummaryResponse(Booking booking) {
        return BookingSummaryResponse.builder()
                .id(booking.getId())
                .guestId(booking.getGuest() != null ? booking.getGuest().getId() : null)
                .unitId(booking.getUnitId())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .status(booking.getStatus())
                .build();
    }
}
