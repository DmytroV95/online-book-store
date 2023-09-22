package com.varukha.onlinebookstore.dto.shoppingcart;

import java.util.Set;
import lombok.Data;

@Data
public class CreateShoppingCartRequestDto {
    private Long userId;
    private Set<Long> cartItems;
}
