package org.example.financeservice.repository;

import org.example.financeservice.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    Optional<PaymentMethod> findByMethodNameIgnoreCase(String methodName);
}
