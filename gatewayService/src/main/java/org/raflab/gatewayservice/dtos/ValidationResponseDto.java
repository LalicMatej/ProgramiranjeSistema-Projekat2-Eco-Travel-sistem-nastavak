package org.raflab.gatewayservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ValidationResponseDto {
    private String username;
    private Set<RoleDto> roles;
}
