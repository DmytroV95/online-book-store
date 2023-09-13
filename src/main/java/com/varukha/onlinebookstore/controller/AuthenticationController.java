package com.varukha.onlinebookstore.controller;

import com.varukha.onlinebookstore.dto.user.request.UserLoginRequestDto;
import com.varukha.onlinebookstore.dto.user.response.UserLoginResponseDto;
import com.varukha.onlinebookstore.dto.user.request.UserRegistrationRequestDto;
import com.varukha.onlinebookstore.dto.user.response.UserRegistrationResponseDto;
import com.varukha.onlinebookstore.exception.RegistrationException;
import com.varukha.onlinebookstore.security.AuthenticationService;
import com.varukha.onlinebookstore.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User authentication management",
        description = "Endpoints for managing user authentication operations")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(summary = "User login operation",
            description = "Login registered user to system")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }

    @PostMapping("/register")
    @Operation(summary = "User register operation",
            description = "Register new user to system")
    public UserRegistrationResponseDto register(
            @RequestBody @Valid UserRegistrationRequestDto request)
            throws RegistrationException {
        return userService.register(request);
    }
}
