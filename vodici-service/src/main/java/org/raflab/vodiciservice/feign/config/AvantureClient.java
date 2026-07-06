package org.raflab.vodiciservice.feign.config;

import org.raflab.vodiciservice.feign.dto.AdventuresResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(
        name = "avanture-service",
        url = "${avanture.service.url}",
        configuration = AvantureFeignConfig.class
)
public interface AvantureClient {

    @GetMapping("/api/avanture/adventures/all")
    List<AdventuresResponse> getAllAdventures();
}
