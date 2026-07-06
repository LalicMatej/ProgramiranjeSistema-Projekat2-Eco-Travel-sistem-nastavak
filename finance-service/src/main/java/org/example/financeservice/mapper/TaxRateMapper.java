package org.example.financeservice.mapper;

import org.example.financeservice.dto.finance.TaxRateResponse;
import org.example.financeservice.entity.TaxRate;
import org.springframework.stereotype.Component;

@Component
public class TaxRateMapper {

    public TaxRateResponse toResponse(TaxRate taxRate) {
        return TaxRateResponse.builder()
                .id(taxRate.getId())
                .taxName(taxRate.getTaxName())
                .percentage(taxRate.getPercentage())
                .build();
    }
}
