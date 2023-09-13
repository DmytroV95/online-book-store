package com.varukha.onlinebookstore.service.user;

import com.varukha.onlinebookstore.dto.user.request.UserRegistrationRequestDto;
import com.varukha.onlinebookstore.dto.user.response.UserRegistrationResponseDto;
import com.varukha.onlinebookstore.exception.RegistrationException;

public interface UserService {
    UserRegistrationResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException;
}
