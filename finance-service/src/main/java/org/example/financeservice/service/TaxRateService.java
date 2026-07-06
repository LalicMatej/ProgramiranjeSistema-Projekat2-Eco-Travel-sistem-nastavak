package org.example.financeservice.service;

import lombok.RequiredArgsConstructor;
import org.example.financeservice.dto.finance.CreateTaxRateRequest;
import org.example.financeservice.dto.finance.TaxRateResponse;
import org.example.financeservice.entity.TaxRate;
import org.example.financeservice.exception.BadRequestException;
import org.example.financeservice.exception.ResourceNotFoundException;
import org.example.financeservice.mapper.TaxRateMapper;
import org.example.financeservice.repository.TaxRateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaxRateService {

    private static final BigDecimal MAX_PERCENTAGE = new BigDecimal("100.00");

    private final TaxRateRepository taxRateRepository;
    private final TaxRateMapper taxRateMapper;

    @Transactional
    public void createTaxRate(CreateTaxRateRequest request) {
        String normalizedTaxName = request.getTaxName().trim();
        validatePercentage(request.getPercentage());

        taxRateRepository.findByTaxNameIgnoreCase(normalizedTaxName)
                .ifPresent(existingTaxRate -> {
                    throw new BadRequestException("Tax rate with name '" + normalizedTaxName + "' already exists.");
                });

        TaxRate taxRate = TaxRate.builder()
                .taxName(normalizedTaxName)
                .percentage(request.getPercentage())
                .build();

        taxRateRepository.save(taxRate);
    }

    @Transactional(readOnly = true)
    public List<TaxRateResponse> getAllTaxRates() {
        return taxRateRepository.findAll()
                .stream()
                .map(taxRateMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaxRateResponse getTaxRateById(Long id) {
        TaxRate taxRate = taxRateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tax rate with id " + id + " was not found."));

        return taxRateMapper.toResponse(taxRate);
    }

    private void validatePercentage(BigDecimal percentage) {
        if (percentage.compareTo(MAX_PERCENTAGE) > 0) {
            throw new BadRequestException("Tax rate percentage cannot be greater than 100.");
        }
    }
}
