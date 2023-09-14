package com.varukha.onlinebookstore.service.user.impl;

import static com.varukha.onlinebookstore.model.Role.RoleName.ROLE_USER;

import com.varukha.onlinebookstore.dto.user.request.UserRegistrationRequestDto;
import com.varukha.onlinebookstore.dto.user.response.UserRegistrationResponseDto;
import com.varukha.onlinebookstore.exception.RegistrationException;
import com.varukha.onlinebookstore.mapper.UserMapper;
import com.varukha.onlinebookstore.model.Role;
import com.varukha.onlinebookstore.model.User;
import com.varukha.onlinebookstore.repository.role.RoleRepository;
import com.varukha.onlinebookstore.repository.user.UserRepository;
import java.util.NoSuchElementException;
import java.util.Set;
import com.varukha.onlinebookstore.service.user.UserService;
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
        if (isUserExist(request.getEmail())) {
            throw new RegistrationException("Unable to complete registration");
        }
        User user = createUser(request);
        User savedUser = userRepository.save(user);
        return userMapper.toUserResponse(savedUser);
    }

    private boolean isUserExist(String email) {
        return userRepository.findUserByEmail(email).isPresent();
    }

    private User createUser(UserRegistrationRequestDto request) {
        User user = userMapper.toModel(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setShippingAddress(request.getShippingAddress());
        Role defaultRole = getDefaultRole();
        user.setRole(Set.of(defaultRole));
        return user;
    }

    private Role getDefaultRole() {
        return roleRepository.findByName(ROLE_USER)
                .orElseThrow(() -> new NoSuchElementException("The role "
                        + ROLE_USER + " is not found in the database"));
    }
}
