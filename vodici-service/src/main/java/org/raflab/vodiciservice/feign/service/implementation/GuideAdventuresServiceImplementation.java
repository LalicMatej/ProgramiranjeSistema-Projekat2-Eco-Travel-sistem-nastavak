package org.raflab.vodiciservice.feign.service.implementation;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.raflab.vodiciservice.feign.config.AvantureClient;
import org.raflab.vodiciservice.feign.dto.AdventuresResponse;
import org.raflab.vodiciservice.feign.service.GuideAdventuresService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GuideAdventuresServiceImplementation implements GuideAdventuresService {

    private final AvantureClient avantureClient;

    @Override
    @CircuitBreaker(name = "avantureService", fallbackMethod = "getAdventuresForGuideFallback")
    @RateLimiter(name = "avantureServiceRateLimit", fallbackMethod = "getAdventuresForGuideRateLimitFallback")
    @Retry(name = "avantureService")
    public Object getAdventuresForGuide(Long guideId) {
        List<AdventuresResponse> all = avantureClient.getAllAdventures();
        List<AdventuresResponse> filtered = all.stream()
                .filter(a -> a.guide_id() != null && a.guide_id().equals(guideId))
                .toList();
        return filtered;
    }

    public Object getAdventuresForGuideFallback(Long guideId, Throwable t) {
        if (t instanceof FeignException.NotFound) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Adventures for guide " + guideId + " not found in Avanture-service");
        }
        if (t instanceof ResponseStatusException) {
            throw (ResponseStatusException) t;
        }
        Map<String, Object> response = new HashMap<>();
        response.put("guideId", guideId);
        response.put("message", "Adventures are temporarily unavailable.");
        response.put("adventures", List.of());
        return response;
    }
    public Object getAdventuresForGuideRateLimitFallback(Long guideId, Throwable t) {
        throw new ResponseStatusException(
                HttpStatus.TOO_MANY_REQUESTS,
                "Previse zahteva prema Avanture-servisu. Pokusajte ponovo kasnije."
        );
    }
}
