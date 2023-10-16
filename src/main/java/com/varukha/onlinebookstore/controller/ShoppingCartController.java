package com.varukha.onlinebookstore.controller;

import com.varukha.onlinebookstore.dto.cartitem.CartItemDto;
import com.varukha.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.varukha.onlinebookstore.dto.cartitem.UpdateCartItemRequestDto;
import com.varukha.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.varukha.onlinebookstore.service.shoppingcart.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management",
        description = "Endpoints for managing shopping cart")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/carts")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Save the new cart item",
            description = "Save the new cart item to shopping cart")
    public CartItemDto saveCartItem(@RequestBody @Valid CreateCartItemRequestDto requestDto) {
        return shoppingCartService.createCartItem(requestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get shopping cart by user id",
            description = "Get shopping cart with all added cart items"
                    + " by user identification number")
    public ShoppingCartDto getShoppingCart() {
        return shoppingCartService.getShoppingCart();
    }

    @DeleteMapping("cart-items/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Delete the cart item by user id",
            description = "Delete the recently added cart items from"
                    + " shopping cart by shopping cart identification number")
    public void deleteCartItemById(@PathVariable Long id) {
        shoppingCartService.deleteById(id);
    }

    @PutMapping("cart-items/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Update the cart item quantity by cart item id",
            description = "Update the cart item quantity in shopping"
                    + " cart by cart item identification number")
    public CartItemDto update(@PathVariable @Valid Long id,
                              @RequestBody UpdateCartItemRequestDto cartItemRequestDto) {
        return shoppingCartService.updateCartItemQuantity(id, cartItemRequestDto);
    }
}
