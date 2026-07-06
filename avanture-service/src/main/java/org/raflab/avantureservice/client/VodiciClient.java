package org.raflab.avantureservice.client;

import org.raflab.avantureservice.dto.response.CertificationsResponse;
import org.raflab.avantureservice.dto.response.GuideResponse;
import org.raflab.avantureservice.dto.response.VacationsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "vodici-service", url = "${vodici.service.url}", configuration = FeignClientConfig.class)
public interface VodiciClient {

    @GetMapping(path = "/api/vodici/guide/guideid/{id}")
    GuideResponse getGuideById(@PathVariable("id") Long id);

    @GetMapping(path = "/api/vodici/vacations/{ime}/{prezime}")
    List<VacationsResponse> getVacationsByImeAndPrezime(@PathVariable("ime") String ime, @PathVariable("prezime") String prezime);

    @GetMapping(path = "/api/vodici/certifications/{ime}/{prezime}")
    List<CertificationsResponse> getCertificationsByImeAndPrezime(@PathVariable("ime") String ime, @PathVariable("prezime") String prezime);
}
