package odrzavanjemikroservis.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import odrzavanjemikroservis.dtos.WorkerDto;
import odrzavanjemikroservis.dtos.WorkerStatisticsDto;
import odrzavanjemikroservis.service.WorkerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/worker")
@Tag(name = "Radnici", description = "API za upravljanje radnicima")
@Validated
@RefreshScope
public class WorkerController {

    private static final Logger log = LoggerFactory.getLogger(WorkerController.class);
    @Autowired
    private WorkerService workerService;

    @PostMapping("/save")
    @RateLimiter(name = "createWorker")
    @Operation(summary = "Kreiraj novog radnika",
            description = "Dodaje novog radnika u sistem")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Radnik uspešno kreiran"),
            @ApiResponse(responseCode = "400", description = "Neispravni podaci"),
            @ApiResponse(responseCode = "429", description = "Previše zahteva - rate limit dostignut")
    })
    public ResponseEntity<WorkerDto> saveWorker(
            @Valid @RequestBody WorkerDto workerDto) {
        log.info("event=endpoint_call method=POST path=/api/worker/save controller=WorkerController action=saveWorker");
        log.info("Saving worker");
        WorkerDto saved = workerService.addWorker(workerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/workers")
    @Operation(summary = "Pretraga radnika po specijalnosti",
            description = "Vraća listu radnika. Ako specijalnost nije navedena, vraća sve radnike")
    @ApiResponse(responseCode = "200", description = "Uspešno pronađeni radnici")
    public ResponseEntity<List<WorkerDto>> getWorkersBySpeciality(
            @RequestParam(required = false)
            @Parameter(description = "Specijalnost radnika (opciono)",
                    example = "Elektrotehničar")
            String speciality) {
        log.info("event=endpoint_call method=GET path=/api/worker/workers controller=WorkerController action=getWorkersBySpeciality");
        log.info("Fetching workers by speciality: {}", speciality);
        return ResponseEntity.ok(workerService.getWorkersBySpeciality(speciality));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Obriši radnika",
            description = "Briše radnika iz sistema po ID-u")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Radnik uspešno obrisan"),
            @ApiResponse(responseCode = "404", description = "Radnik nije pronađen")
    })
    public ResponseEntity<Void> deleteWorker(
            @PathVariable @Positive @Parameter(description = "ID radnika", example = "1")
            Long id) {
        log.info("event=endpoint_call method=DELETE path=/api/worker/{id} controller=WorkerController action=deleteWorker");
        log.info("Deleting worker id: {}", id);
        workerService.deleteWorker(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statsBySpeciality")
    @Operation(summary = "Izlistaj specijalnosti radnika i statistike o njima",
            description = "Izlistava specijalnosti svih radnika u sistemu sa dodatnim informacijama.")
    public ResponseEntity<List<WorkerStatisticsDto>> getStatsBySpeciality(){
        log.info("event=endpoint_call method=GET path=/api/worker/statsBySpeciality controller=WorkerController action=getStatsBySpeciality");
        log.info("Fetching worker statistics by speciality");
        List<WorkerStatisticsDto> u = new ArrayList<>();
        u=workerService.getWorkerStatisticsBySpeciality();
        return ResponseEntity.ok(u);
    }
}