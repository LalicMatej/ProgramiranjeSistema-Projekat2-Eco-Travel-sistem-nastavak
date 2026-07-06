// smestajmikroservis/controllers/UnitController.java
package smestajmikroservis.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import smestajmikroservis.dtos.*;
import smestajmikroservis.services.AddressService;
import smestajmikroservis.services.UnitService;
import smestajmikroservis.dtos.WorkOrderDto;  // DODAJ OVO (kopiran DTO)

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/units")
@Tag(name = "Smeštajne jedinice", description = "API za upravljanje smeštajnim jedinicama")
@Validated
@RefreshScope
public class UnitController {

    private static final Logger log = LoggerFactory.getLogger(UnitController.class);

    @Autowired
    private UnitService unitService;

    @Autowired
    private AddressService addressService;


    @PostMapping("/add")
    @RateLimiter(name = "createUnit")
    @Operation(summary = "Kreiraj novu smeštajnu jedinicu",
            description = "Dodaje novu jedinicu u sistem")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Jedinica uspešno kreirana"),
            @ApiResponse(responseCode = "400", description = "Neispravni podaci"),
            @ApiResponse(responseCode = "429", description = "Previše zahteva - rate limit dostignut")
    })
    public ResponseEntity<UnitDto> createUnit(@Valid @RequestBody UnitDto unitDto) {
        log.info("event=endpoint_call method=POST path=/api/units/add controller=UnitController action=createUnit");
        log.info("Creating unit: {}", unitDto.getName());
        UnitDto created = unitService.add(unitDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Dohvati jedinicu po ID",
            description = "Vraća smeštajnu jedinicu sa zadatim ID-jem")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jedinica uspešno pronađena"),
            @ApiResponse(responseCode = "404", description = "Jedinica nije pronađena")
    })
    public ResponseEntity<UnitDto> getUnitById(@PathVariable Long id) {
        log.info("event=endpoint_call method=GET path=/api/units/{id} controller=UnitController action=getUnitById");
        log.info("Fetching unit by id: {}", id);
        return ResponseEntity.ok(unitService.getUnitById(id));
    }

    @GetMapping("/{id}/exists")
    @Operation(summary = "Proveri da li jedinica postoji")
    public ResponseEntity<Boolean> unitExists(@PathVariable Long id) {
        log.info("event=endpoint_call method=GET path=/api/units/{id}/exists controller=UnitController action=unitExists");
        log.info("Checking existence of unit id: {}", id);
        boolean exists = unitService.existsById(id);

        return ResponseEntity.ok(exists);
    }

    @GetMapping("/search")
    @Operation(summary = "Pretraga jedinica po imenu",
            description = "Vraća listu jedinica koje sadrže zadati naziv")
    @ApiResponse(responseCode = "200", description = "Uspešno pronađene jedinice")
    @ApiResponse(responseCode = "404", description = "Ne postoje jedinice sa tim imenom")
    public ResponseEntity<List<UnitDto>> searchUnitByName(
            @RequestParam
            @Size(min = 2, message = "Naziv za pretragu mora imati najmanje 2 karaktera")
            @Parameter(description = "Naziv za pretragu", example = "Apartman")
            String name) {
        log.info("event=endpoint_call method=GET path=/api/units/search controller=UnitController action=searchUnitByName");
        log.info("Searching units by name: {}", name);
        List<UnitDto> response = unitService.findByName(name);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/findByParameters")
    @Operation(summary = "Pretraga po parametrima",
            description = "Filtriranje jedinica po tipu i ceni")
    @ApiResponse(responseCode = "200", description = "Uspešno filtrirane jedinice")
    public ResponseEntity<List<UnitDto>> findByParameters(
            @RequestParam(required = false)
            @Parameter(description = "Tip smeštaja", example = "APARTMAN")
            String type,
            @RequestParam(required = false)
            @DecimalMin(value = "0", message = "Minimalna cena ne može biti negativna")
            @Parameter(description = "Minimalna cena", example = "50")
            Double minPrice,
            @RequestParam(required = false)
            @DecimalMin(value = "0", message = "Maksimalna cena ne može biti negativna")
            @Parameter(description = "Maksimalna cena", example = "200")
            Double maxPrice) {

        log.info("event=endpoint_call method=GET path=/api/units/findByParameters controller=UnitController action=findByParameters");
        log.info("Searching units by type: {}, minPrice: {}, maxPrice: {}", type, minPrice, maxPrice);
        List<UnitDto> unitDtos = unitService.searchByTypeAndPrice(type, minPrice, maxPrice);
        return ResponseEntity.ok(unitDtos);
    }

    @GetMapping("/all")
    @Operation(summary = "Prikaži sve jedinice",
            description = "Vraća listu svih smeštajnih jedinica")
    @ApiResponse(responseCode = "200", description = "Uspešno prikazane sve jedinice")
    public ResponseEntity<List<UnitDto>> findAll() {
        log.info("event=endpoint_call method=GET path=/api/units/all controller=UnitController action=findAll");
        log.info("Fetching all units");
        return ResponseEntity.ok(unitService.findAll());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Obriši jedinicu",
            description = "Briše smeštajnu jedinicu po ID-u")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jedinica uspešno obrisana"),
            @ApiResponse(responseCode = "404", description = "Jedinica nije pronađena")
    })
    public ResponseEntity<String> deleteUnit(
            @PathVariable
            @Positive(message = "ID mora biti pozitivan broj")
            @Parameter(description = "ID jedinice", example = "1")
            Long id) {
        log.info("event=endpoint_call method=DELETE path=/api/units/{id} controller=UnitController action=deleteUnit");
        log.info("Deleting unit id: {}", id);
        unitService.delete(id);
        return ResponseEntity.ok("Jedinica uspešno obrisana");
    }

    @GetMapping("/city")
    @Operation(summary = "Pretraga po gradu",
            description = "Vraća sve jedinice u zadatom gradu")
    @ApiResponse(responseCode = "200", description = "Uspešno pronađene jedinice")
    public ResponseEntity<List<UnitDto>> findByCity(
            @RequestParam
            @Size(min = 2, message = "Naziv grada mora imati najmanje 2 karaktera")
            @Parameter(description = "Naziv grada", example = "Beograd")
            String city) {
        log.info("event=endpoint_call method=GET path=/api/units/city controller=UnitController action=findByCity");
        log.info("Fetching units by city: {}", city);
        return ResponseEntity.ok(addressService.findByCity(city));
    }

    @GetMapping("/{unitId}/calculate-price")
    @Operation(summary = "Izračunaj cenu",
            description = "Izračunava ukupnu cenu za zadati period")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cena uspešno izračunata"),
            @ApiResponse(responseCode = "404", description = "Jedinica nije pronađena")
    })
    public ResponseEntity<Double> calculatePrice(
            @PathVariable
            @Positive(message = "ID jedinice mora biti pozitivan broj")
            @Parameter(description = "ID jedinice", example = "1")
            Long unitId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Future(message = "Datum početka mora biti u budućnosti")
            @Parameter(description = "Datum početka", example = "2026-05-01")
            LocalDate startDate,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Future(message = "Datum završetka mora biti u budućnosti")
            @Parameter(description = "Datum završetka", example = "2026-05-07")
            LocalDate endDate) {

        log.info("event=endpoint_call method=GET path=/api/units/{unitId}/calculate-price controller=UnitController action=calculatePrice");
        log.info("Calculating price for unit id: {}, from {} to {}", unitId, startDate, endDate);
        return ResponseEntity.ok(unitService.calculatePrice(unitId, startDate, endDate));
    }

    @GetMapping("/{unitId}/with-work-orders")
    @Operation(summary = "Dohvati jedinicu sa radnim nalozima",
            description = "Vraća smeštajnu jedinicu zajedno sa svim radnim nalozima iz održavanje servisa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Podaci uspesno pronadjeni"),
            @ApiResponse(responseCode = "404", description = "Jedinica nije pronadjena"),
            @ApiResponse(responseCode = "503", description = "Odrzavanje servis nije dostupan")
    })
    public ResponseEntity<UnitWithWorkOrdersDto> getUnitWithWorkOrders(@PathVariable Long unitId) {
        log.info("event=endpoint_call method=GET path=/api/units/{unitId}/with-work-orders controller=UnitController action=getUnitWithWorkOrders");
        log.info("Fetching unit with work orders for unit id: {}", unitId);
            return ResponseEntity.ok(unitService.getUnitWithOrders(unitId));


    }

    @GetMapping("/unitsWithsWorksInProgress")
    @Operation(summary = "Dohvati jedinice na kojima se trenutno odrzavaju radovi",
            description = "Vraca jedinice koje su pod radovima (IN_PROGRESS)")
    public ResponseEntity<List<UnitDto>> getUnitsWithsWorksInProgress() {
        log.info("event=endpoint_call method=GET path=/api/units/unitsWithsWorksInProgress controller=UnitController action=getUnitsWithsWorksInProgress");
        log.info("Fetching units with work orders in progress");
        List<UnitDto> u = unitService.getUnitsWithWorksInProgress();

        return ResponseEntity.ok(u);
    }

    @GetMapping("/cityStatistics")
    @Operation(summary = "Dohvati statistike svih gradova",
            description = "Vraca se lista gradova sa svojim statistikama")
    public ResponseEntity<List<CityStatisticsDto>> getCityStatistics() {
        log.info("event=endpoint_call method=GET path=/api/units/cityStatistics controller=UnitController action=getCityStatistics");
        log.info("Fetching city statistics");
        List<CityStatisticsDto> u = unitService.getCityStatistics();
        return ResponseEntity.ok(u);
    }

}