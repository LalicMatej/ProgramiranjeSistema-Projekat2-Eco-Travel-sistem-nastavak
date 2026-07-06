package org.example.financeservice.dto.integration;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class RemoteBookingPricePreviewResponse {
    Long bookingId;
    BigDecimal basePrice;
    BigDecimal addOnTotal;
    BigDecimal totalPrice;
}
