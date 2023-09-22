package com.varukha.onlinebookstore.mapper;

import com.varukha.onlinebookstore.config.MapperConfig;
import com.varukha.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.varukha.onlinebookstore.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {

    @Mapping(source = "shoppingCart.user.id", target = "userId")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

}
