package com.varukha.onlinebookstore.service;

import com.varukha.onlinebookstore.dto.user.UserRegistrationRequestDto;
import com.varukha.onlinebookstore.dto.user.UserRegistrationResponseDto;
import com.varukha.onlinebookstore.exception.RegistrationException;

public interface UserService {
    UserRegistrationResponseDto register(UserRegistrationRequestDto request) throws RegistrationException;

}
