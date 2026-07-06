package smestajmikroservis.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import smestajmikroservis.dtos.PriceTierDto;
import smestajmikroservis.services.PriceTierService;

import java.util.List;

@RequestMapping("/api/priceTiers")
@RestController
public class PriceTierController {

    private static final Logger log = LoggerFactory.getLogger(PriceTierController.class);

    @Autowired
    private PriceTierService priceTierService;

    // ===== METODA ZA ČUVANJE - vraća ResponseEntity<PriceTierDto> =====
    @PostMapping("/add")
    @Operation(summary = "Kreiraj novi cenovnik",
            description = "Dodaje novi cenovnik za određenu smeštajnu jedinicu")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cenovnik uspešno kreiran"),
            @ApiResponse(responseCode = "400", description = "Neispravni podaci"),
            @ApiResponse(responseCode = "404", description = "Jedinica nije pronađena"),
            @ApiResponse(responseCode = "409", description = "Cenovnik se preklapa sa postojećim")
    })
    public ResponseEntity<PriceTierDto> savePriceTier(@Valid @RequestBody PriceTierDto priceTierDto) {
        log.info("event=endpoint_call method=POST path=/api/priceTiers/add controller=PriceTierController action=savePriceTier");
        log.info("Saving price tier: {}", priceTierDto.getName());
        PriceTierDto saved = priceTierService.savePriceTier(priceTierDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }




//    @GetMapping(path="/all")
//    public List<PriceTier> getAllPriceTiers() {
//        return priceTierRepository.findAll();
//    }

}
