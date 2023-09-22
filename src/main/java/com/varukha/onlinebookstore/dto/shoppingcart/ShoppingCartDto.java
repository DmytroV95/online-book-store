package com.varukha.onlinebookstore.dto.shoppingcart;

import com.varukha.onlinebookstore.dto.cartitem.CartItemDto;
import java.util.Set;
import lombok.Data;

@Data
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItemDto> cartItems;
}
