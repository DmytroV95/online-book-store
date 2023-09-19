package com.varukha.onlinebookstore.service.shoppingcart;

import com.varukha.onlinebookstore.dto.cartitem.CartItemDto;
import com.varukha.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.varukha.onlinebookstore.dto.cartitem.UpdateCartItemRequestDto;
import com.varukha.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.varukha.onlinebookstore.model.CartItem;
import com.varukha.onlinebookstore.model.User;

public interface ShoppingCartService {
    void create(User user);

    CartItemDto save(CreateCartItemRequestDto requestDto);

    CartItem getCartItemById(Long id);

    ShoppingCartDto getShoppingCartByUserId(Long id);

    void deleteById(Long id);

    CartItemDto updateCartItemQuantity(Long id, UpdateCartItemRequestDto cartItemRequestDto);
}