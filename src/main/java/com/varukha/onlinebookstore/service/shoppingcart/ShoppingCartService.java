package com.varukha.onlinebookstore.service.shoppingcart;

import com.varukha.onlinebookstore.dto.cartitem.CartItemDto;
import com.varukha.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.varukha.onlinebookstore.dto.cartitem.UpdateCartItemRequestDto;
import com.varukha.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.varukha.onlinebookstore.model.User;

public interface ShoppingCartService {
    void createShoppingCartForUser(User user);

    CartItemDto createCartItem(CreateCartItemRequestDto requestDto);

    ShoppingCartDto getShoppingCart();

    void deleteById(Long id);

    CartItemDto updateCartItemQuantity(Long id, UpdateCartItemRequestDto cartItemRequestDto);

    void clearShoppingCart(Long userId);
}
