package com.varukha.onlinebookstore.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;
import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

@Data
public class CreateBookRequestDto {
    @NotNull
    @NotBlank
    @Length(min = 1, max = 255)
    private String title;
    @NotNull
    @NotBlank
    @Length(min = 1, max = 255)
    private String author;
    @NotNull
    @NotBlank
    @ISBN
    @Length(min = 13, max = 13)
    private String isbn;
    @NotNull
    @Min(0)
    private BigDecimal price;
    @NotNull
    @NotBlank
    @Length(min = 5, max = 255)
    private String description;
    @NotNull
    @NotBlank
    @URL
    private String coverImage;
}
