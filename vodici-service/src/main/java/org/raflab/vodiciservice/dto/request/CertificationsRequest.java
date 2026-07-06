package org.raflab.vodiciservice.dto.request;

import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.raflab.vodiciservice.model.Guides;

import java.time.LocalDate;

@Data
public class CertificationsRequest {
    @NotEmpty(message = "Polje name mora biti uneseno")
    private String name;
    @NotEmpty(message = "Polje issuing_body mora biti uneseno")
    private String issuing_body;
    @NotNull(message = "Polje expiry_date mora biti uneseno")
    private LocalDate expiry_date;
    @NotNull(message = "Polje guideRequest mora biti uneseno")
    private GuideRequest guidesRequest;
    @NotNull(message = "Polje certificationLevel mora biti uneseno")
    private Integer certificationLevel;
}
