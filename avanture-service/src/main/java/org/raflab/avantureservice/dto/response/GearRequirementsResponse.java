package org.raflab.avantureservice.dto.response;

import lombok.Data;

@Data
public class GearRequirementsResponse {
    private Long id;
    private String item_name;
    private boolean is_mandatory;
    private AdventuresResponse adventuresResponse;
}
