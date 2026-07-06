package org.example.financeservice.dto.integration;

import lombok.Builder;
import lombok.Value;
import org.example.financeservice.dto.integration.enums.RemoteBookingStatus;

import java.time.LocalDate;

@Value
@Builder
public class RemoteBookingSummaryResponse {
    Long id;
    Long guestId;
    Long unitId;
    LocalDate startDate;
    LocalDate endDate;
    RemoteBookingStatus status;
}
