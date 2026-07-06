package org.example.bookingservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookingservice.dto.guest.CreateGuestRequest;
import org.example.bookingservice.dto.guest.GuestResponse;
import org.example.bookingservice.service.GuestService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/guests")
@RequiredArgsConstructor
@Tag(name = "Guests", description = "Endpoints for creating and retrieving guest records.")
public class GuestController {

    private final GuestService guestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create guest", description = "Registers a new guest in the booking service.")
    public void createGuest(@Valid @RequestBody CreateGuestRequest request) {
        guestService.createGuest(request);
    }

    @GetMapping
    @Operation(summary = "List guests", description = "Returns all registered guests.")
    public List<GuestResponse> getAllGuests() {
        return guestService.getAllGuests();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get guest by id", description = "Returns a single guest by identifier.")
    public GuestResponse getGuestById(@PathVariable Long id) {
        return guestService.getGuestById(id);
    }
}
