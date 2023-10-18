package com.varukha.onlinebookstore.controller;

import com.varukha.onlinebookstore.dto.order.CreateOrderRequestDto;
import com.varukha.onlinebookstore.dto.order.OrderDto;
import com.varukha.onlinebookstore.dto.order.UpdateOrderStatusRequestDto;
import com.varukha.onlinebookstore.dto.orderitem.OrderItemDto;
import com.varukha.onlinebookstore.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management",
        description = "Endpoints for managing orders")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Place an order",
            description = "Place an order, to purchase the books in user's shopping cart")
    public OrderDto create(@RequestBody @Valid CreateOrderRequestDto orderRequestDto) {
        return orderService.create(orderRequestDto);
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @Operation(summary = "Get an order history",
            description = "Get an order history, to track user's past purchases")
    public List<OrderDto> getAll(Pageable pageable) {
        return orderService.getAll(pageable);
    }

    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @Operation(summary = "Get all OrderItems in user's order",
            description = "Retrieve all OrderItems for a specific order by order"
                    + " identification number")
    public Set<OrderItemDto> getAllOrderItemsByOrderId(@PathVariable @Valid Long orderId) {
        return orderService.getAllOrderItemsByOrderId(orderId);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @Operation(summary = "Get the OrderItems from order by order id",
            description = "Retrieve a specific OrderItem within an order by order"
                    + " identification number")
    public OrderItemDto getOrderItemFromOrderById(@PathVariable Long orderId,
                                                  @PathVariable Long itemId) {
        return orderService.getOrderItemFromOrderById(orderId, itemId);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Update order status by order id",
            description = "Update order status by order identification number,"
                    + " to manage the order processing workflow.")
    public OrderDto updateOrderStatus(@PathVariable Long id,
                                      @RequestBody @Valid
                                      UpdateOrderStatusRequestDto statusRequestDto) {
        return orderService.updateOrderStatus(id, statusRequestDto);
    }
}
