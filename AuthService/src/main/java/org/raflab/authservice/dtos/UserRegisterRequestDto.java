package org.raflab.authservice.dtos;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterRequestDto {
    private String username;
    private Set<String> roles;
}
