package odrzavanjemikroservis.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import odrzavanjemikroservis.dtos.UnitDto;
import odrzavanjemikroservis.dtos.AvailabilityCalendarDto;
import java.util.List;

@FeignClient(
        name = "smestaj-service",
        url = "${odrzavanje.smestaj.url}",
        fallback = SmestajClientFallback.class,
        configuration = SmestajClientConfig.class
)
public interface SmestajClient {

    // METODA 1
    @GetMapping("/api/units/all")
    List<UnitDto> getAllUnits();

    // METODA 2
    @GetMapping("/api/units/{id}")
    UnitDto getUnitById(@PathVariable("id") Long id);

    // METODA 3
    @GetMapping("/api/units/search")
    List<UnitDto> searchUnitsByName(@RequestParam("name") String name);

    // METODA 4
    @GetMapping("/api/units/city")
    List<UnitDto> findByCity(@RequestParam("city") String city);

    // METODA 5
    @GetMapping("/api/units/findByParameters")
    List<UnitDto> findByParameters(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice
    );

    // METODA 6
    @GetMapping("/api/units/{unitId}/calculate-price")
    Double calculatePrice(
            @PathVariable("unitId") Long unitId,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate
    );

    // METODA 7
    @PostMapping("/api/reserve/add")
    AvailabilityCalendarDto addReservation(@RequestBody AvailabilityCalendarDto reservation);

    @GetMapping("/api/units/{id}/exists")
    Boolean unitExists(@PathVariable("id") Long id);
}