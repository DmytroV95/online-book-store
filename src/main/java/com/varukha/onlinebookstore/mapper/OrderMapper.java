package com.varukha.onlinebookstore.mapper;

import com.varukha.onlinebookstore.config.MapperConfig;
import com.varukha.onlinebookstore.dto.order.OrderDto;
import com.varukha.onlinebookstore.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    @Mapping(source = "order.id", target = "id")
    @Mapping(source = "order.user.id", target = "userId")
    OrderDto toDto(Order order);
}
