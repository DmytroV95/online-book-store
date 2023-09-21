package com.varukha.onlinebookstore.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserLoginRequestDto {
    @NotNull
    @NotBlank
    @Length(min = 4, max = 50)
    private String email;
    @NotNull
    @NotBlank
    @Length(min = 6, max = 100)
    private String password;
}
