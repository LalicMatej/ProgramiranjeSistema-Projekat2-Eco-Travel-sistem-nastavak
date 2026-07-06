package org.raflab.authservice.service;

import lombok.RequiredArgsConstructor;
import org.raflab.authservice.dtos.UserRegisterRequestDto;
import org.raflab.authservice.dtos.UserRegisterResponseDto;
import org.raflab.authservice.dtos.ValidationResponseDto;
import org.raflab.authservice.entity.Role;
import org.raflab.authservice.entity.User;
import org.raflab.authservice.exception.ResourceNotFoundException;
import org.raflab.authservice.repository.RoleRepository;
import org.raflab.authservice.repository.UserRepository;
import org.raflab.authservice.utils.EntityMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EntityMapper entityMapper;

    public ValidationResponseDto validate(String apiKey) {
        User u = userRepository.findByApiKey(apiKey)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with api key:" + apiKey));

        return entityMapper.toValidationResponse(u);
    }

    public ValidationResponseDto findByUsername(String username){
        User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username:" + username));

        return entityMapper.toValidationResponse(u);
    }

    public UserRegisterResponseDto register(UserRegisterRequestDto dto) {
        // provera za role
         Set<Role> roles= dto.getRoles().stream()
                .map(name->roleRepository.findByRole(name)
                                .orElseThrow(() -> new ResourceNotFoundException("Role not found with name:" + name)))
                .collect(Collectors.toSet());

        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        User u = new User();
        u.setUsername(dto.getUsername());
        u.setRoles(roles);
        u.setApiKey(uuid);

        return entityMapper.userToUserRegisterResponseDto(userRepository.save(u));
    }
}
