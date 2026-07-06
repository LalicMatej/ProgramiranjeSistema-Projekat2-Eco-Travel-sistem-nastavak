package org.example.bookingservice.service;

import org.example.bookingservice.dto.system.SystemStatusResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class SystemInfoService {

    public SystemStatusResponse getStatus() {
        return SystemStatusResponse.builder()
                .serviceName("booking-service")
                .version("v1-zadatak1")
                .timestamp(Instant.now())
                .status("UP")
                .build();
    }
}
