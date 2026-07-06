package org.example.bookingservice.mapper;

import org.example.bookingservice.dto.guest.GuestResponse;
import org.example.bookingservice.entity.Guest;
import org.springframework.stereotype.Component;

@Component
public class GuestMapper {

    public GuestResponse toResponse(Guest guest) {
        return GuestResponse.builder()
                .id(guest.getId())
                .firstName(guest.getFirstName())
                .lastName(guest.getLastName())
                .email(guest.getEmail())
                .build();
    }
}
