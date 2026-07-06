package org.example.bookingservice.repository;

import org.example.bookingservice.entity.CancellationPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CancellationPolicyRepository extends JpaRepository<CancellationPolicy, Long> {
    Optional<CancellationPolicy> findByPolicyNameIgnoreCase(String policyName);
}
