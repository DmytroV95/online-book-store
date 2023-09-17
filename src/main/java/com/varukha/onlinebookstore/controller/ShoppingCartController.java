package com.varukha.onlinebookstore.controller;

import com.varukha.onlinebookstore.dto.cartItem.CartItemDto;
import com.varukha.onlinebookstore.dto.shoppingCart.CreateShoppingCartRequestDto;
import com.varukha.onlinebookstore.dto.shoppingCart.ShoppingCartDto;
import com.varukha.onlinebookstore.service.cartItem.CartItemService;
import com.varukha.onlinebookstore.service.shoppingCart.ShoppingCartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/cart ")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
//    private final CartItemService cartItemService;


//    @GetMapping
//    public List<CartItemDto> getAll(Pageable pageable) {
//        return cartItemService.getAll(pageable);
//    }

    public void deleteCartItemById(Long id) {

    }

}
