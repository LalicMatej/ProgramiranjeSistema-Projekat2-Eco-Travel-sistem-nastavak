package org.example.bookingservice.mapper;

import org.example.bookingservice.dto.cancellationpolicy.CancellationPolicyResponse;
import org.example.bookingservice.entity.CancellationPolicy;
import org.springframework.stereotype.Component;

@Component
public class CancellationPolicyMapper {

    public CancellationPolicyResponse toResponse(CancellationPolicy cancellationPolicy) {
        return CancellationPolicyResponse.builder()
                .id(cancellationPolicy.getId())
                .policyName(cancellationPolicy.getPolicyName())
                .refundPercentage(cancellationPolicy.getRefundPercentage())
                .build();
    }
}
