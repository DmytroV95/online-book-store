package com.varukha.onlinebookstore.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserRegistrationRequestDto {
    @NotNull
    @NotBlank
    @Length(min = 4, max = 50)
    private String email;
    @NotNull
    @NotBlank
    @Length(min = 6, max = 100)
    private String password;
    @NotNull
    @NotBlank
    @Length(min = 6, max = 100)
    private String repeatPassword ;
    @NotNull
    @NotBlank
    @Length(min = 2, max = 50)
    private String firstName;
    @NotNull
    @NotBlank
    @Length(min = 2, max = 50)
    private String lastName;
    @NotNull
    @NotBlank
    @Length(min = 6, max = 255)
    private String shippingAddress;
}
