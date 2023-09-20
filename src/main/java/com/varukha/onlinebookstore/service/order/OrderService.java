package com.varukha.onlinebookstore.service.order;

import com.varukha.onlinebookstore.dto.order.CreateOrderRequestDto;
import com.varukha.onlinebookstore.dto.order.OrderDto;
import com.varukha.onlinebookstore.dto.order.UpdateOrderStatusRequestDto;
import com.varukha.onlinebookstore.dto.orderitem.OrderItemResponseDto;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface OrderService {
    OrderDto create(CreateOrderRequestDto orderRequestDto);

    List<OrderDto> getAll(Pageable pageable);

    Set<OrderItemResponseDto> getAllOrderItemsByOrderId(Long orderId);

    OrderItemResponseDto getOrderItemFromOrderById(Long orderId, Long itemId);

    OrderDto updateOrderStatus(Long id,
                               UpdateOrderStatusRequestDto statusRequestDto);
}
