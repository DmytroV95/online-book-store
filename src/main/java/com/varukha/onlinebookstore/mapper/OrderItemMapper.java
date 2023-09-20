package com.varukha.onlinebookstore.mapper;

import com.varukha.onlinebookstore.config.MapperConfig;
import com.varukha.onlinebookstore.dto.orderitem.OrderItemResponseDto;
import com.varukha.onlinebookstore.model.CartItem;
import com.varukha.onlinebookstore.model.Order;
import com.varukha.onlinebookstore.model.OrderItem;
import java.util.Optional;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    OrderItem cartItemToOrderItem(CartItem cartItem);

    OrderItemResponseDto cartItemToOrderItemResponseDto(OrderItem orderItem);


    @AfterMapping
    default void initializeOrderInItem(@MappingTarget
                                       OrderItem orderItem, CartItem cartItem, Order order) {
        orderItem.setOrder(order);
        orderItem.setBook(cartItem.getBook());
        orderItem.setPrice(cartItem.getBook().getPrice());
    }

}

