package com.varukha.onlinebookstore.dto.book.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
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
