package org.example.financeservice.repository;

import org.example.financeservice.entity.TaxRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaxRateRepository extends JpaRepository<TaxRate, Long> {
    Optional<TaxRate> findByTaxNameIgnoreCase(String taxName);
}
