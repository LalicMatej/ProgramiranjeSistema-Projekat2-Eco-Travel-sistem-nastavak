package org.example.bookingservice.service;

import lombok.RequiredArgsConstructor;
import org.example.bookingservice.dto.guest.CreateGuestRequest;
import org.example.bookingservice.dto.guest.GuestResponse;
import org.example.bookingservice.entity.Guest;
import org.example.bookingservice.exception.BadRequestException;
import org.example.bookingservice.exception.ResourceNotFoundException;
import org.example.bookingservice.mapper.GuestMapper;
import org.example.bookingservice.repository.GuestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GuestService {

    private final GuestRepository guestRepository;
    private final GuestMapper guestMapper;

    @Transactional
    public void createGuest(CreateGuestRequest request) {
        guestRepository.findByEmailIgnoreCase(request.getEmail())
                .ifPresent(guest -> {
                    throw new BadRequestException("Guest with email '" + request.getEmail() + "' already exists.");
                });

        Guest guest = Guest.builder()
                .firstName(request.getFirstName().trim())
                .lastName(request.getLastName().trim())
                .email(request.getEmail().trim().toLowerCase())
                .build();

        guestRepository.save(guest);
    }

    @Transactional(readOnly = true)
    public List<GuestResponse> getAllGuests() {
        return guestRepository.findAll()
                .stream()
                .map(guestMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public GuestResponse getGuestById(Long id) {
        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guest with id " + id + " was not found."));

        return guestMapper.toResponse(guest);
    }
}
