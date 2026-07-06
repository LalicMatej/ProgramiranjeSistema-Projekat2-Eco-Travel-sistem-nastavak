package org.example.bookingservice.repository;

import org.example.bookingservice.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    Optional<Guest> findByEmailIgnoreCase(String email);
}
