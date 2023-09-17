package com.varukha.onlinebookstore.dto.cartItem;

import lombok.Data;

@Data
public class CreateCartItemRequestDto {
    private Long bookId;
    private int quantity;
}
