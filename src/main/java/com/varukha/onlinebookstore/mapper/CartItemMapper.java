package com.varukha.onlinebookstore.mapper;

import com.varukha.onlinebookstore.config.MapperConfig;
import com.varukha.onlinebookstore.dto.cartItem.CartItemDto;
import com.varukha.onlinebookstore.dto.cartItem.CreateCartItemRequestDto;
import com.varukha.onlinebookstore.dto.shoppingCart.CreateShoppingCartRequestDto;
import com.varukha.onlinebookstore.dto.shoppingCart.ShoppingCartDto;
import com.varukha.onlinebookstore.model.CartItem;
import com.varukha.onlinebookstore.model.ShoppingCart;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import java.util.stream.Collectors;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    CartItemDto toDto(CartItem cartItem);

    CartItem toModel(CreateCartItemRequestDto requestDto);


}
