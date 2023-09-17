package com.varukha.onlinebookstore.mapper;

import com.varukha.onlinebookstore.dto.shoppingCart.CreateShoppingCartRequestDto;
import com.varukha.onlinebookstore.dto.shoppingCart.ShoppingCartDto;
import com.varukha.onlinebookstore.model.CartItem;
import com.varukha.onlinebookstore.model.ShoppingCart;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.MappingTarget;

import java.util.stream.Collectors;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    ShoppingCart toModel(CreateShoppingCartRequestDto requestDto);

    @AfterMapping
    default void setCartItemIds(@MappingTarget ShoppingCartDto shoppingCartDto,
                                ShoppingCart shoppingCart) {
        if (shoppingCart.getCartItems() != null) {
            shoppingCartDto.setCartItemIds(shoppingCart.getCartItems().stream()
                    .map(CartItem::getId)
                    .collect(Collectors.toSet()));
        }
    }

    @AfterMapping
    default void setCartItem(@MappingTarget CreateShoppingCartRequestDto shoppingCartRequestDto,
                             ShoppingCart shoppingCart) {
        shoppingCart.setCartItems(shoppingCartRequestDto.getCartItemIds().stream()
                .map(CartItem::new)
                .collect(Collectors.toSet()));
    }
}
