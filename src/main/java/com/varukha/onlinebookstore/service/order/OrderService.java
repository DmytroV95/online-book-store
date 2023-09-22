package com.varukha.onlinebookstore.service.order;

import com.varukha.onlinebookstore.dto.order.CreateOrderRequestDto;
import com.varukha.onlinebookstore.dto.order.OrderDto;
import com.varukha.onlinebookstore.dto.order.UpdateOrderStatusRequestDto;
import com.varukha.onlinebookstore.dto.orderitem.OrderItemDto;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto create(CreateOrderRequestDto orderRequestDto);

    List<OrderDto> getAll(Pageable pageable);

    Set<OrderItemDto> getAllOrderItemsByOrderId(Long orderId);

    OrderItemDto getOrderItemFromOrderById(Long orderId, Long itemId);

    OrderDto updateOrderStatus(Long id,
                               UpdateOrderStatusRequestDto statusRequestDto);
}
