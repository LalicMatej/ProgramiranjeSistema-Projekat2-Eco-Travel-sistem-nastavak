package org.example.bookingservice.repository;

import org.example.bookingservice.entity.AddOnItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddOnItemRepository extends JpaRepository<AddOnItem, Long> {
    List<AddOnItem> findByBookingId(Long bookingId);
}
