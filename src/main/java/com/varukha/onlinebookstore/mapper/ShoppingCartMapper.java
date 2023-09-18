package com.varukha.onlinebookstore.mapper;

import com.varukha.onlinebookstore.config.MapperConfig;
import com.varukha.onlinebookstore.dto.shoppingcart.CreateShoppingCartRequestDto;
import com.varukha.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.varukha.onlinebookstore.model.CartItem;
import com.varukha.onlinebookstore.model.ShoppingCart;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {

    @Mapping(source = "shoppingCart.user.id", target = "userId")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    @AfterMapping
    default void setCartItems(@MappingTarget ShoppingCart shoppingCart,
                              CreateShoppingCartRequestDto shoppingCartDto) {
        shoppingCart.setCartItems(shoppingCartDto.getCartItems().stream()
                .map(CartItem::new)
                .collect(Collectors.toSet()));
    }
}
