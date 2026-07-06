package odrzavanjemikroservis.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import odrzavanjemikroservis.dtos.MaintenanceStatisticsDto;
import odrzavanjemikroservis.dtos.WorkOrderDto;
import odrzavanjemikroservis.service.WorkOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workOrder")
@Tag(name = "Radni nalozi", description = "API za upravljanje radnim nalozima održavanja")
@Validated
@RefreshScope
public class WorkOrderController {

    private static final Logger log = LoggerFactory.getLogger(WorkOrderController.class);
    @Autowired
    private WorkOrderService workOrderService;

    @PostMapping("/save")
    @RateLimiter(name = "createWorkOrder")
    @Operation(summary = "Kreiraj novi radni nalog",
            description = "Čuva radni nalog u sistemu")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Radni nalog uspešno kreiran"),
            @ApiResponse(responseCode = "400", description = "Neispravni podaci"),
            @ApiResponse(responseCode = "429", description = "Previše zahteva - rate limit dostignut")
    })
    public ResponseEntity<WorkOrderDto> saveWorkOrder(
            @Valid @RequestBody WorkOrderDto workOrderDto) {
        log.info("event=endpoint_call method=POST path=/api/workOrder/save controller=WorkOrderController action=saveWorkOrder");
        log.info("Saving work order for unit id: {}", workOrderDto.getUnitId());
        WorkOrderDto saved = workOrderService.save(workOrderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/all")
    @Operation(summary = "Prikaži sve radne naloge",
            description = "Vraća listu svih radnih naloga")
    @ApiResponse(responseCode = "200", description = "Uspešno prikazani svi nalozi")
    public ResponseEntity<List<WorkOrderDto>> getAllWorkOrders() {
        log.info("event=endpoint_call method=GET path=/api/workOrder/all controller=WorkOrderController action=getAllWorkOrders");
        log.info("Fetching all work orders");
        return ResponseEntity.ok(workOrderService.getAllWorkOrders());
    }

    @GetMapping("/status/{id}")
    @Operation(summary = "Prikaži status radnog naloga",
            description = "Vraća trenutni status radnog naloga za zadati ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status uspešno pronađen"),
            @ApiResponse(responseCode = "404", description = "Radni nalog nije pronađen")
    })
    public ResponseEntity<String> getWorkOrderStatus(
            @PathVariable @Positive @Parameter(description = "ID radnog naloga", example = "1")
            Long id) {
        log.info("event=endpoint_call method=GET path=/api/workOrder/status/{id} controller=WorkOrderController action=getWorkOrderStatus");
        log.info("Fetching status for work order id: {}", id);
        return ResponseEntity.ok(workOrderService.getCurrentStatus(id));
    }

    @PatchMapping("/updateStatus/{id}")
    @RateLimiter(name = "updateWorkOrderStatus")
    @Operation(summary = "Ažuriraj status radnog naloga",
            description = "Menja status radnog naloga i opciono dodaje beleške")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status uspešno ažuriran"),
            @ApiResponse(responseCode = "400", description = "Neispravan zahtev"),
            @ApiResponse(responseCode = "404", description = "Radni nalog nije pronađen"),
            @ApiResponse(responseCode = "429", description = "Previše zahteva - rate limit dostignut")
    })
    public ResponseEntity<String> updateWorkOrderStatus(
            @PathVariable @Positive @Parameter(description = "ID radnog naloga", example = "1")
            Long id,
            @RequestParam(required = false)
            @Parameter(description = "Opcija beleške za radni nalog", example = "Potreban dodatni materijal")
            String notes) {
        log.info("event=endpoint_call method=PATCH path=/api/workOrder/updateStatus/{id} controller=WorkOrderController action=updateWorkOrderStatus");
        log.info("Updating status for work order id: {}", id);
        workOrderService.updateStatus(id, notes);
        return ResponseEntity.ok("Status uspešno ažuriran");
    }
    // odrzavanjemikroservis/controller/WorkOrderController.java

    @GetMapping("/byUnit/{unitId}")
    @Operation(summary = "Dohvati radne naloge za jedinicu",
            description = "Vraća sve radne naloge za zadatu smeštajnu jedinicu")
    @ApiResponse(responseCode = "200", description = "Uspešno pronađeni nalozi")
    public ResponseEntity<List<WorkOrderDto>> getWorkOrdersByUnitId(
            @PathVariable @Positive Long unitId) {
        log.info("event=endpoint_call method=GET path=/api/workOrder/byUnit/{unitId} controller=WorkOrderController action=getWorkOrdersByUnitId");
        log.info("Fetching work orders for unit id: {}", unitId);
        List<WorkOrderDto> workOrders = workOrderService.getWorkOrdersByUnitId(unitId);
        return ResponseEntity.ok(workOrders);
    }

    @GetMapping("/statistics/{unitId}")
    @Operation(summary = "Dohvati statistiku popravki za prosledjeni Unit id",
            description = "Vraca statistiku popravki za zadatu smeštajnu jedinicu")
    @ApiResponse(responseCode = "200", description = "Pronadjena statistika za unit")
    public ResponseEntity<MaintenanceStatisticsDto> getStatisticsByUnitId(@PathVariable @Positive Long unitId) {
        log.info("event=endpoint_call method=GET path=/api/workOrder/statistics/{unitId} controller=WorkOrderController action=getStatisticsByUnitId");
        log.info("Fetching maintenance statistics for unit id: {}", unitId);
        return ResponseEntity.ok(workOrderService.getStatisticsByUnitId(unitId));
    }

    @GetMapping("/inProgress")
    @Operation(summary = "Uzmi sve radne naloge koji su u toku",
            description = "Vraca radne naloge koji su u toku")
    public ResponseEntity<List<WorkOrderDto>> getInProgressWorkOrders()
    {
        log.info("event=endpoint_call method=GET path=/api/workOrder/inProgress controller=WorkOrderController action=getInProgressWorkOrders");
        log.info("Fetching all in-progress work orders");
        return ResponseEntity.ok(workOrderService.getAllWorkOrdersInProggress());
    }
}