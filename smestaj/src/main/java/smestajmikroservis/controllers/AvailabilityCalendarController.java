package smestajmikroservis.controllers;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smestajmikroservis.dtos.AvailabilityCalendarDto;
import smestajmikroservis.services.AvailabilityCalendarService;

@RestController
@RequestMapping("/api/reserve")
@Tag(name = "Rezervacije", description = "API za upravljanje rezervacijama")
@RefreshScope
public class AvailabilityCalendarController {

    private static final Logger log = LoggerFactory.getLogger(AvailabilityCalendarController.class);

    @Autowired
    private AvailabilityCalendarService availabilityCalendarService;

    @PostMapping("/add")
    @RateLimiter(name = "createReservation")
    @Operation(summary = "Kreiraj rezervaciju",
            description = "Dodaje novu rezervaciju u kalendar dostupnosti")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rezervacija uspešno kreirana"),
            @ApiResponse(responseCode = "400", description = "Neispravni podaci"),
            @ApiResponse(responseCode = "409", description = "Termin je već zauzet"),
            @ApiResponse(responseCode = "429", description = "Previše zahteva - rate limit dostignut")
    })
    public ResponseEntity<?> addReservation(
            @Valid @RequestBody AvailabilityCalendarDto availabilityCalendarDto) {

        log.info("event=endpoint_call method=POST path=/api/reserve/add controller=AvailabilityCalendarController action=addReservation");
        log.info("Adding reservation for unit type: {}", availabilityCalendarDto.getUnitType());
        availabilityCalendarDto.setUnitType(availabilityCalendarDto.getUnitType().toUpperCase());


        //  provera da li je endDate posle startDate
        if (availabilityCalendarDto.getEndDate().isBefore(availabilityCalendarDto.getStartDate())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Datum završetka mora biti posle datuma početka");
        }

        try{
       //u servisu provera
            return ResponseEntity.ok(availabilityCalendarService.addReservation(availabilityCalendarDto));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}