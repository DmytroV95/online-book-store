package com.varukha.onlinebookstore.controller;

import com.varukha.onlinebookstore.dto.user.UserLoginRequestDto;
import com.varukha.onlinebookstore.dto.user.UserLoginResponseDto;
import com.varukha.onlinebookstore.dto.user.UserRegistrationRequestDto;
import com.varukha.onlinebookstore.dto.user.UserRegistrationResponseDto;
import com.varukha.onlinebookstore.exception.RegistrationException;
import com.varukha.onlinebookstore.security.AuthenticationService;
import com.varukha.onlinebookstore.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User authentication management",
        description = "Endpoints for managing user authentication")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }

    @PostMapping("/register")
    public UserRegistrationResponseDto register(@RequestBody @Valid UserRegistrationRequestDto request)
            throws RegistrationException {
        return userService.register(request);
    }
}
