package com.varukha.onlinebookstore.dto.cartitem;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateCartItemRequestDto {
    @Min(0)
    private int quantity;
}
