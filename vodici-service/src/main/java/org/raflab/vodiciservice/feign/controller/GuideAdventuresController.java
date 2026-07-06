package org.raflab.vodiciservice.feign.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.raflab.vodiciservice.feign.service.GuideAdventuresService;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vodici")
@RequiredArgsConstructor
@Tag(name = "Guide Adventures", description = "Read-only adventures pulled from Avanture-service")
@RefreshScope
public class GuideAdventuresController {

    private final GuideAdventuresService guideAdventuresService;

    @GetMapping("/guide/{guideId}/adventures")
    @Operation(summary = "Get adventures for a guide")
    public ResponseEntity<Object> getAdventuresForGuide(
            @Parameter(description = "Guide ID") @PathVariable Long guideId) {
        return ResponseEntity.ok(guideAdventuresService.getAdventuresForGuide(guideId));
    }
}
