package org.example.financeservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.financeservice.dto.finance.CreateTaxRateRequest;
import org.example.financeservice.dto.finance.TaxRateResponse;
import org.example.financeservice.service.TaxRateService;
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
@RequestMapping("/api/tax-rates")
@RequiredArgsConstructor
@Tag(name = "Tax Rates", description = "Endpoints for managing tax rate definitions.")
public class TaxRateController {

    private final TaxRateService taxRateService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create tax rate", description = "Creates a new tax rate entry.")
    public void createTaxRate(@Valid @RequestBody CreateTaxRateRequest request) {
        taxRateService.createTaxRate(request);
    }

    @GetMapping
    @Operation(summary = "List tax rates", description = "Returns all configured tax rates.")
    public List<TaxRateResponse> getAllTaxRates() {
        return taxRateService.getAllTaxRates();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get tax rate by id", description = "Returns a single tax rate by identifier.")
    public TaxRateResponse getTaxRateById(@PathVariable Long id) {
        return taxRateService.getTaxRateById(id);
    }
}
