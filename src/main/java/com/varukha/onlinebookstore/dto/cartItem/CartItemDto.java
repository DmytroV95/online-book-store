package com.varukha.onlinebookstore.dto.cartItem;

import lombok.Data;

@Data
public class CartItemDto {
    private Long bookId;
    private int quantity;
    private Long shoppingCart;
}
