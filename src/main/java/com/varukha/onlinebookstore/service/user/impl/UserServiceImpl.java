package com.varukha.onlinebookstore.service.user.impl;

import static com.varukha.onlinebookstore.model.Role.RoleName.ROLE_USER;

import com.varukha.onlinebookstore.dto.user.UserRegistrationRequestDto;
import com.varukha.onlinebookstore.dto.user.UserRegistrationResponseDto;
import com.varukha.onlinebookstore.exception.RegistrationException;
import com.varukha.onlinebookstore.mapper.UserMapper;
import com.varukha.onlinebookstore.model.Role;
import com.varukha.onlinebookstore.model.User;
import com.varukha.onlinebookstore.repository.role.RoleRepository;
import com.varukha.onlinebookstore.repository.user.UserRepository;
import com.varukha.onlinebookstore.service.user.UserService;
import java.util.NoSuchElementException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @Override
    public UserRegistrationResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException {
        if (userRepository.findUserByEmail(request.getEmail()).isPresent()) {
            throw new RegistrationException("Unable complete registration");
        }
        User user = userMapper.toModel(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setShippingAddress(request.getShippingAddress());
        Role defaultRole = roleRepository.findByName(ROLE_USER)
                .orElseThrow(() -> new NoSuchElementException("The role "
                        + ROLE_USER + " is not found in the database"));
        user.setRole(Set.of(defaultRole));
        User savedUser = userRepository.save(user);
        return userMapper.toUserResponse(savedUser);
    }
}