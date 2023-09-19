package com.varukha.onlinebookstore.dto.cartitem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemRequestDto {
    @NotNull
    @Min(0)
    private Integer quantity;
}
