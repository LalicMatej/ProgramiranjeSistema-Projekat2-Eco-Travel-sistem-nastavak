package odrzavanjemikroservis.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import odrzavanjemikroservis.dtos.MaintenanceTaskDto;
import odrzavanjemikroservis.service.MaintenanceTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenanceTask")
@Tag(name = "Zadaci održavanja", description = "API za upravljanje zadacima održavanja")
@RefreshScope
public class MaintenanceTaskController {

    private static final Logger log = LoggerFactory.getLogger(MaintenanceTaskController.class);

    @Autowired
    private MaintenanceTaskService maintenanceTaskService;

    @PostMapping("/save")
    @RateLimiter(name = "createMaintenanceTask")
    @Operation(summary = "Kreiraj novi zadatak održavanja",
            description = "Dodaje novi tip zadatka održavanja u sistem")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Zadatak uspešno kreiran"),
            @ApiResponse(responseCode = "400", description = "Neispravni podaci"),
            @ApiResponse(responseCode = "429", description = "Previše zahteva - rate limit dostignut")
    })
    public ResponseEntity<MaintenanceTaskDto> saveMaintenanceTask(
            @Valid @RequestBody MaintenanceTaskDto maintenanceTask) {
        log.info("event=endpoint_call method=POST path=/api/maintenanceTask/save controller=MaintenanceTaskController action=saveMaintenanceTask");
        log.info("Saving maintenance task ");
        MaintenanceTaskDto saved = maintenanceTaskService.createMaintenanceTask(maintenanceTask);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/all")
    @Operation(summary = "Prikaži sve zadatke održavanja",
            description = "Vraća listu svih dostupnih zadataka održavanja")
    @ApiResponse(responseCode = "200", description = "Uspešno prikazani svi zadaci")
    public ResponseEntity<List<MaintenanceTaskDto>> getAllMaintenanceTasks() {
        log.info("event=endpoint_call method=GET path=/api/maintenanceTask/all controller=MaintenanceTaskController action=getAllMaintenanceTasks");
        log.info("Fetching all maintenance tasks");
        return ResponseEntity.ok(maintenanceTaskService.getAllMaintenanceTasks());
    }
}