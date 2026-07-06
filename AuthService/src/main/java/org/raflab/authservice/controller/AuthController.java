package org.raflab.authservice.controller;

import org.raflab.authservice.dtos.UserRegisterResponseDto;
import org.raflab.authservice.dtos.ValidationResponseDto;
import org.raflab.authservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.raflab.authservice.dtos.UserRegisterRequestDto;
import org.raflab.authservice.dtos.UserRegisterResponseDto;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/validate")
    public ValidationResponseDto validate(@RequestHeader("X-API-KEY") String apiKey){
        log.info("Validating API key");
        return userService.validate(apiKey);
    }

    @PostMapping("/register")
    public UserRegisterResponseDto register(@RequestBody UserRegisterRequestDto dto){
        return userService.register(dto);
    }

    @GetMapping("/find/{username}")
    public ValidationResponseDto findByUsername(@PathVariable("username") String username){
        return userService.findByUsername(username);
    }

}
