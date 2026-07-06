package org.example.bookingservice.repository;

import org.example.bookingservice.entity.BookingStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingStatusLogRepository extends JpaRepository<BookingStatusLog, Long> {
}
