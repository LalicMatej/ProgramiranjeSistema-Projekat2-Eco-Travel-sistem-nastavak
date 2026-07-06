package org.example.financeservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.financeservice.dto.finance.CreatePaymentMethodRequest;
import org.example.financeservice.dto.finance.PaymentMethodResponse;
import org.example.financeservice.service.PaymentMethodService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/payment-methods")
@RequiredArgsConstructor
@Tag(name = "Payment Methods", description = "Endpoints for managing payment methods used by finance transactions.")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create payment method", description = "Creates a new payment method entry.")
    public void createPaymentMethod(@Valid @RequestBody CreatePaymentMethodRequest request) {
        paymentMethodService.createPaymentMethod(request);
    }

    @GetMapping
    @Operation(summary = "List payment methods", description = "Returns all configured payment methods.")
    public List<PaymentMethodResponse> getAllPaymentMethods() {
        return paymentMethodService.getAllPaymentMethods();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get payment method by id", description = "Returns a single payment method by identifier.")
    public PaymentMethodResponse getPaymentMethodById(@PathVariable Long id) {
        return paymentMethodService.getPaymentMethodById(id);
    }
}
