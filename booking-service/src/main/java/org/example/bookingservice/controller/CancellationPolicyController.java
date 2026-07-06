package org.example.bookingservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookingservice.dto.cancellationpolicy.CancellationPolicyResponse;
import org.example.bookingservice.dto.cancellationpolicy.CreateCancellationPolicyRequest;
import org.example.bookingservice.service.CancellationPolicyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cancellation-policies")
@RequiredArgsConstructor
@Tag(name = "Cancellation Policies", description = "Endpoints for managing cancellation policy definitions.")
public class CancellationPolicyController {

    private final CancellationPolicyService cancellationPolicyService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create cancellation policy", description = "Creates a new cancellation policy entry.")
    public void createPolicy(@Valid @RequestBody CreateCancellationPolicyRequest request) {
        cancellationPolicyService.createPolicy(request);
    }

    @GetMapping
    @Operation(summary = "List cancellation policies", description = "Returns all configured cancellation policies.")
    public List<CancellationPolicyResponse> getAllPolicies() {
        return cancellationPolicyService.getAllPolicies();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get cancellation policy by id", description = "Returns a single cancellation policy by its identifier.")
    public CancellationPolicyResponse getPolicyById(@PathVariable Long id) {
        return cancellationPolicyService.getPolicyById(id);
    }
}
