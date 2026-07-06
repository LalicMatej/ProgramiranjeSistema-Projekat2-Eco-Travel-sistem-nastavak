package org.raflab.vodiciservice.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CertificationsResponse {
    private Long id;
    private String name;
    private String issuing_body;
    private LocalDate expiry_date;
    private Integer certificationLevel;
    private GuideResponse guideResponse;
}
