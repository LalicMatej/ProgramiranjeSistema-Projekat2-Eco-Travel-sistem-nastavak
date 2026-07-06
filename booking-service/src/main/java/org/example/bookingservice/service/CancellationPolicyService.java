package org.example.bookingservice.service;

import lombok.RequiredArgsConstructor;
import org.example.bookingservice.dto.cancellationpolicy.CancellationPolicyResponse;
import org.example.bookingservice.dto.cancellationpolicy.CreateCancellationPolicyRequest;
import org.example.bookingservice.entity.CancellationPolicy;
import org.example.bookingservice.exception.BadRequestException;
import org.example.bookingservice.exception.ResourceNotFoundException;
import org.example.bookingservice.mapper.CancellationPolicyMapper;
import org.example.bookingservice.repository.CancellationPolicyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CancellationPolicyService {

    private final CancellationPolicyRepository cancellationPolicyRepository;
    private final CancellationPolicyMapper cancellationPolicyMapper;

    @Transactional
    public void createPolicy(CreateCancellationPolicyRequest request) {
        cancellationPolicyRepository.findByPolicyNameIgnoreCase(request.getPolicyName())
                .ifPresent(policy -> {
                    throw new BadRequestException("Cancellation policy '" + request.getPolicyName() + "' already exists.");
                });

        CancellationPolicy cancellationPolicy = CancellationPolicy.builder()
                .policyName(request.getPolicyName().trim())
                .refundPercentage(request.getRefundPercentage())
                .build();

        cancellationPolicyRepository.save(cancellationPolicy);
    }

    @Transactional(readOnly = true)
    public List<CancellationPolicyResponse> getAllPolicies() {
        return cancellationPolicyRepository.findAll()
                .stream()
                .map(cancellationPolicyMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CancellationPolicyResponse getPolicyById(Long id) {
        CancellationPolicy cancellationPolicy = cancellationPolicyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cancellation policy with id " + id + " was not found."));

        return cancellationPolicyMapper.toResponse(cancellationPolicy);
    }
}
