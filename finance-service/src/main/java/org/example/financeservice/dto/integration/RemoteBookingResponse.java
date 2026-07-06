package org.example.financeservice.dto.integration;

import lombok.Builder;
import lombok.Value;
import org.example.financeservice.dto.integration.enums.RemoteBookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
@Builder
public class RemoteBookingResponse {
    Long id;
    Long guestId;
    Long unitId;
    Long policyId;
    LocalDate startDate;
    LocalDate endDate;
    BigDecimal totalPrice;
    RemoteBookingStatus status;
}
