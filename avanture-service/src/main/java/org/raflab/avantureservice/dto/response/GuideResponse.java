package org.raflab.avantureservice.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GuideResponse {
    private Long id;
    private String first_name;
    private String last_name;
}
