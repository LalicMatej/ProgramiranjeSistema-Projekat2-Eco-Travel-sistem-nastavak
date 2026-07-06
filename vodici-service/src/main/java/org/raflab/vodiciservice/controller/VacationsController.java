package org.raflab.vodiciservice.controller;

import jakarta.validation.Valid;
import org.raflab.vodiciservice.dto.request.CertificationsRequest;
import org.raflab.vodiciservice.dto.request.VacationsRequest;
import org.raflab.vodiciservice.dto.response.CertificationsResponse;
import org.raflab.vodiciservice.dto.response.VacationsResponse;
import org.raflab.vodiciservice.service.CertificationsService;
import org.raflab.vodiciservice.service.VacationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8083")
@RestController
@RequestMapping("/api/vodici")
@RefreshScope
public class VacationsController {
    @Autowired
    private VacationsService vacationsService;
    //v6
    @Value("${custom.welcome-message}")
    private String message;

    @GetMapping("/vacations/welcome")
    public String getMessage() { return message; }

    @PostMapping(path = "/vacations/add")
    public void addCertification(@RequestBody @Valid VacationsRequest vacationsRequest) {
        vacationsService.save(vacationsRequest);
    }

    @GetMapping(path = "/vacations/all")
    public List<VacationsResponse> getAllVacations() {
        return vacationsService.getAllVacations();
    }

    @GetMapping(path = "/vacations/{id}")
    public VacationsResponse getVacationById(@PathVariable Long id)
    {
        return vacationsService.findbyid(id);
    }

    @PutMapping(path = "/vacations/update/{id}")
    public void updateVacation(@RequestBody @Valid VacationsRequest vacationsRequest,@PathVariable Long id){
        vacationsService.updateById(vacationsRequest,id);
    }

    @DeleteMapping(path = "/vacations/delete/{id}")
    public void deleteVacationById(@PathVariable Long id) {
        vacationsService.deleteById(id);
    }

    @GetMapping(path = "/vacations/{ime}/{prezime}")
    public List<VacationsResponse> getVacationsByImeAndPrezime(@PathVariable String ime, @PathVariable String prezime)
    {
        return vacationsService.getVacationsByImeAndPrezime(ime,prezime);
    }
}
