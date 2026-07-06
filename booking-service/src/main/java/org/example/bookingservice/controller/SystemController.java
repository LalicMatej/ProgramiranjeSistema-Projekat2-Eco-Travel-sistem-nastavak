package org.example.bookingservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.bookingservice.dto.system.SystemStatusResponse;
import org.example.bookingservice.service.SystemInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system")
@RequiredArgsConstructor
@Tag(name = "System", description = "Operational endpoints for the booking service.")
public class SystemController {

    private final SystemInfoService systemInfoService;

    @GetMapping("/status")
    @Operation(summary = "Get service status", description = "Returns basic metadata and current runtime status for the booking service.")
    public SystemStatusResponse status() {
        return systemInfoService.getStatus();
    }
}
