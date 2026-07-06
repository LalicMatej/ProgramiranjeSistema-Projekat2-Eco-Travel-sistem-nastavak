package org.example.financeservice.integration;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BookingClientResult<T> {
    IntegrationResultStatus status; // Status koji je dobijen posle request-a ka booking servisu
    Long bookingId; // Booking id da bi se znalo za koju rezervaciju je fejlovao (laksi logging)
    T payload; // Sam payload, za sad imamo tri DTO objekta koje booking servis moze da posalje
    String message; // Dodatna poruka o uspesnoj komunikaciji, greski...

    public boolean isSuccess() {
        return status == IntegrationResultStatus.SUCCESS;
    }
}
