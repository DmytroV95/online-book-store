package com.varukha.onlinebookstore.dto.book.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;

import jakarta.validation.constraints.Size;
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
//    @Schema(example = "948-3-16-148410-0")
//    @ISBN
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
    @NotNull
    @Size(min = 1)
    private Set<Long> categoriesId;
}
