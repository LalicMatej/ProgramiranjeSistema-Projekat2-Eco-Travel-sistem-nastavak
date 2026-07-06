package org.raflab.vodiciservice.dto.response;

import lombok.Data;

@Data
public class LanguagesResponse {
    private Long id;
    private String language_name;
    private String language_code;
}
