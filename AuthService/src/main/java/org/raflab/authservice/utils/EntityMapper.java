package org.raflab.authservice.utils;

import lombok.AllArgsConstructor;
import org.raflab.authservice.dtos.RoleDto;
import org.raflab.authservice.dtos.UserRegisterRequestDto;
import org.raflab.authservice.dtos.ValidationResponseDto;
import org.raflab.authservice.dtos.UserRegisterResponseDto;
import org.raflab.authservice.entity.Role;
import org.raflab.authservice.entity.User;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EntityMapper {

    public UserRegisterResponseDto userToUserRegisterResponseDto(User user){
        UserRegisterResponseDto dto = new UserRegisterResponseDto();
        dto.setUsername(user.getUsername());
        dto.setApiKey(user.getApiKey());
        dto.setRoles(user.getRoles().stream().map(this::roleToDto).collect(Collectors.toSet()));

        return dto;
    }

    public RoleDto roleToDto(Role role){
        RoleDto dto = new RoleDto();
        dto.setId(role.getId());
        dto.setRole(role.getRole());
        return dto;
    }

    public Role dtoToRole(RoleDto dto){
        Role role = new Role();
        role.setId(dto.getId());
        role.setRole(dto.getRole());
        return role;
    }


    public User UserRegisterResponseDtoToUser(UserRegisterRequestDto dto){
        if (dto == null){
            return null;
        }
        User user = new User();
        user.setUsername(dto.getUsername());

        Set<Role> roles = dto.getRoles().stream().map(roleName -> {
            Role role = new Role();
            role.setRole(roleName);
            return role;
        }).collect(Collectors.toSet());

        user.setRoles(roles);
        return user;
    }

    public ValidationResponseDto toValidationResponse(User user){
        if(user == null)
            return null;

        ValidationResponseDto dto = new ValidationResponseDto();
        dto.setUsername(user.getUsername());
        dto.setRoles(user.getRoles().stream().map(this::roleToDto).collect(Collectors.toSet()));

        return dto;
    }
}
