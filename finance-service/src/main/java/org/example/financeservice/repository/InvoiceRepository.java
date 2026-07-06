package org.example.financeservice.repository;

import org.example.financeservice.entity.Invoice;
import org.example.financeservice.entity.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByStatus(InvoiceStatus status);

    boolean existsByBookingId(Long bookingId);
}
