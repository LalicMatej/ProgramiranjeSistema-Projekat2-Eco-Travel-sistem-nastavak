package org.raflab.gatewayservice.dtos;
import lombok.Data;
import java.util.Set;

@Data
public class AuthResponseDto {
    private boolean valid;
    private Long userId;
    private String username;
    private Set<String> roles;
    private String message;
}