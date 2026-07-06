package org.example.financeservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.financeservice.dto.finance.CreateInvoiceRequest;
import org.example.financeservice.dto.finance.InvoiceResponse;
import org.example.financeservice.dto.finance.InvoiceTotalResponse;
import org.example.financeservice.dto.finance.RegisterTransactionRequest;
import org.example.financeservice.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
@Tag(name = "Invoices", description = "Endpoints for creating invoices and registering payments.")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create invoice", description = "Creates a new invoice and calculates the total amount using the selected tax rate.")
    public void createInvoice(@Valid @RequestBody CreateInvoiceRequest request) {
        invoiceService.createInvoice(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get invoice by id", description = "Returns a single invoice by identifier.")
    public InvoiceResponse getInvoiceById(@PathVariable Long id) {
        return invoiceService.getInvoiceById(id);
    }

    @GetMapping("/{id}/total")
    @Operation(summary = "Get invoice total", description = "Returns subtotal and total amount for a single invoice.")
    public InvoiceTotalResponse getInvoiceTotal(@PathVariable Long id) {
        return invoiceService.getInvoiceTotal(id);
    }

    @PostMapping("/{id}/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register invoice transaction", description = "Registers a payment transaction for an existing invoice.")
    public void registerTransaction(@PathVariable Long id,
                                    @Valid @RequestBody RegisterTransactionRequest request) {
        invoiceService.registerTransaction(id, request);
    }
}
