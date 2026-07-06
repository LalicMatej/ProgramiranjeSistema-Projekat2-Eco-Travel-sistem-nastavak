package odrzavanjemikroservis.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import odrzavanjemikroservis.service.MaintenanceLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/maintenanceLog")
@Tag(name = "Logovi održavanja", description = "API za statistiku i logove održavanja")
@Validated
@RefreshScope
public class MaintenanceLogController {

    private static final Logger log = LoggerFactory.getLogger(MaintenanceLogController.class);

    @Autowired
    private MaintenanceLogService maintenanceLogService;

    @GetMapping("/avgMaintenanceDuration/{unitId}")
    @Operation(summary = "Prosečno trajanje održavanja",
            description = "Izračunava prosečno trajanje održavanja za datu jedinicu (u satima)")
    @ApiResponse(responseCode = "200", description = "Uspešno izračunato prosečno trajanje")
    public ResponseEntity<Double> averageUnitMaintenanceDuration(
            @PathVariable @Positive @Parameter(description = "ID jedinice/opreme", example = "101")
            Long unitId) {
        log.info("event=endpoint_call method=GET path=/api/maintenanceLog/avgMaintenanceDuration/{unitId} controller=MaintenanceLogController action=averageUnitMaintenanceDuration");
        log.info("Fetching average maintenance duration for unit id: {}", unitId);
        double avgHours = maintenanceLogService.avgMaintenanceDurationHours(unitId);
        return ResponseEntity.ok(avgHours);
    }
}