package com.varukha.onlinebookstore.dto.shoppingCart;

import com.varukha.onlinebookstore.model.CartItem;
import lombok.Data;
import java.util.Set;

@Data
public class CreateShoppingCartRequestDto {
    private Long userId;
    private Set<Long> cartItemIds;
}
