package org.example.financeservice.service;

import lombok.RequiredArgsConstructor;
import org.example.financeservice.dto.finance.CreatePaymentMethodRequest;
import org.example.financeservice.dto.finance.PaymentMethodResponse;
import org.example.financeservice.entity.PaymentMethod;
import org.example.financeservice.exception.BadRequestException;
import org.example.financeservice.exception.ResourceNotFoundException;
import org.example.financeservice.mapper.PaymentMethodMapper;
import org.example.financeservice.repository.PaymentMethodRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMethodMapper paymentMethodMapper;

    @Transactional
    public void createPaymentMethod(CreatePaymentMethodRequest request) {
        String normalizedMethodName = request.getMethodName().trim();

        paymentMethodRepository.findByMethodNameIgnoreCase(normalizedMethodName)
                .ifPresent(existingMethod -> {
                    throw new BadRequestException("Payment method with name '" + normalizedMethodName + "' already exists.");
                });

        PaymentMethod paymentMethod = PaymentMethod.builder()
                .methodName(normalizedMethodName)
                .active(request.isActive())
                .build();

        paymentMethodRepository.save(paymentMethod);
    }

    @Transactional(readOnly = true)
    public List<PaymentMethodResponse> getAllPaymentMethods() {
        return paymentMethodRepository.findAll()
                .stream()
                .map(paymentMethodMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PaymentMethodResponse getPaymentMethodById(Long id) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment method with id " + id + " was not found."));

        return paymentMethodMapper.toResponse(paymentMethod);
    }
}
