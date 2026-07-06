package org.example.financeservice.mapper;

import org.example.financeservice.dto.finance.PaymentMethodResponse;
import org.example.financeservice.entity.PaymentMethod;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodMapper {

    public PaymentMethodResponse toResponse(PaymentMethod paymentMethod) {
        return PaymentMethodResponse.builder()
                .id(paymentMethod.getId())
                .methodName(paymentMethod.getMethodName())
                .active(paymentMethod.isActive())
                .build();
    }
}
