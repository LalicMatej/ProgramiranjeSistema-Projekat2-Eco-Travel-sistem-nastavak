package org.raflab.avantureservice.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CertificationsResponse {
    private Integer certificationLevel;
}
