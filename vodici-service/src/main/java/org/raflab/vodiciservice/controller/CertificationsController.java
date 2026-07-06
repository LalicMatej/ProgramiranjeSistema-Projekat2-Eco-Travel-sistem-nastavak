package org.raflab.vodiciservice.controller;

import jakarta.validation.Valid;
import org.raflab.vodiciservice.dto.request.CertificationsRequest;
import org.raflab.vodiciservice.dto.request.GuideRequest;
import org.raflab.vodiciservice.dto.response.CertificationsResponse;
import org.raflab.vodiciservice.dto.response.GuideResponse;
import org.raflab.vodiciservice.service.CertificationsService;
import org.raflab.vodiciservice.service.GuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8083")
@RestController
@RequestMapping("/api/vodici")
@RefreshScope
public class CertificationsController {
    @Autowired
    private CertificationsService certificationsService;
    @PostMapping(path = "/certifications/add")
    public void addCertification(@RequestBody @Valid CertificationsRequest certificationsRequest) {
        certificationsService.save(certificationsRequest);
    }

    @GetMapping(path = "/certifications/all")
    public List<CertificationsResponse> getAllCertifications() {
        return certificationsService.getAllCertifications();
    }

    @GetMapping(path = "/certifications/{id}")
    public CertificationsResponse getCertificationById(@PathVariable Long id)
    {
        return certificationsService.findbyid(id);
    }

    @PutMapping(path = "/certifications/update/{id}")
    public void updateCertification(@RequestBody @Valid CertificationsRequest certificationsRequest,@PathVariable Long id){
        certificationsService.updateById(certificationsRequest,id);
    }

    @DeleteMapping(path = "/certifications/delete/{id}")
    public void deleteCertificationById(@PathVariable Long id) {
        certificationsService.deleteById(id);
    }

    @GetMapping(path = "/certifications/{ime}/{prezime}")
    public List<CertificationsResponse> getCertificationsByImeAndPrezime(@PathVariable String ime, @PathVariable String prezime)
    {
        return certificationsService.getCertificationByImeAndPrezime(ime,prezime);
    }
}
