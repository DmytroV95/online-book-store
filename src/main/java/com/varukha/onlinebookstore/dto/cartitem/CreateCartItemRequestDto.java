package com.varukha.onlinebookstore.dto.cartitem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCartItemRequestDto {
    private Long bookId;
    @NotNull
    @Min(0)
    private Integer quantity;
}
