package org.raflab.authservice.dtos;

import lombok.*;
import org.raflab.authservice.entity.Role;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterResponseDto {
    private String username;
    private Set<RoleDto> roles;
    private String apiKey;
}
