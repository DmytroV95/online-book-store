package com.varukha.onlinebookstore.mapper;

import com.varukha.onlinebookstore.config.MapperConfig;
import com.varukha.onlinebookstore.dto.user.request.UserRegistrationRequestDto;
import com.varukha.onlinebookstore.dto.user.response.UserRegistrationResponseDto;
import com.varukha.onlinebookstore.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserRegistrationResponseDto toUserResponse(User user);

    User toModel(UserRegistrationRequestDto requestDto);
}
