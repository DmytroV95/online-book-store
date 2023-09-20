package com.varukha.onlinebookstore.service.order.impl;

import com.varukha.onlinebookstore.dto.order.CreateOrderRequestDto;
import com.varukha.onlinebookstore.dto.order.OrderDto;
import com.varukha.onlinebookstore.dto.order.UpdateOrderStatusRequestDto;
import com.varukha.onlinebookstore.dto.orderitem.OrderItemResponseDto;
import com.varukha.onlinebookstore.exception.EntityNotFoundException;
import com.varukha.onlinebookstore.mapper.OrderItemMapper;
import com.varukha.onlinebookstore.mapper.OrderMapper;
import com.varukha.onlinebookstore.model.Order;
import com.varukha.onlinebookstore.model.OrderItem;
import com.varukha.onlinebookstore.model.User;
import com.varukha.onlinebookstore.repository.order.OrderRepository;
import com.varukha.onlinebookstore.repository.orderitem.OrderItemRepository;
import com.varukha.onlinebookstore.repository.shoppingcart.ShoppingCartRepository;
import com.varukha.onlinebookstore.service.order.OrderService;
import com.varukha.onlinebookstore.service.user.UserService;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserService userService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    @Override
    public OrderDto create(CreateOrderRequestDto orderRequestDto) {
        Order order = createOrder(orderRequestDto);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public List<OrderDto> getAll(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public Set<OrderItemResponseDto> getAllOrderItemsByOrderId(Long id) {
        return orderRepository.findOrderById(id)
                .map(orderItem -> orderItem.getOrderItems()
                        .stream()
                        .map(orderItemMapper::cartItemToOrderItemResponseDto)
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
    }

    @Override
    public OrderItemResponseDto getOrderItemFromOrderById(Long orderId, Long itemId) {
        return orderItemRepository.findOrderItemByIdAndOrderId(itemId, orderId)
                        .map(orderItemMapper::cartItemToOrderItemResponseDto)
                .orElseThrow();
    }

    @Override
    public OrderDto updateOrderStatus(Long id, UpdateOrderStatusRequestDto statusRequestDto) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setStatus(statusRequestDto.getStatus());
                    orderRepository.save(order);
                    return orderMapper.toDto(order);
                })
                .orElseThrow(() -> new EntityNotFoundException("Order "
                        + "not found by id: " + id));
    }

    private Order createOrder(CreateOrderRequestDto orderRequestDto) {
        User authenticatedUser = userService.getAuthenticatedUser();
        Order order = new Order();
        Long userId = authenticatedUser.getId();
        order.setUser(authenticatedUser);
        order.setStatus(Order.Status.PENDING);
        order.setTotal(calculateTotalCartItemsPrice(userId));
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(orderRequestDto.getShippingAddress());
        order.setOrderItems(getOrderItems(userId));
        return order;
    }

    private BigDecimal calculateTotalCartItemsPrice(Long userId) {
        return shoppingCartRepository.findByUserId(userId)
                .map(shoppingCart -> shoppingCart.getCartItems()
                        .stream()
                        .map(cartItem -> cartItem.getBook().getPrice()
                                .multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .orElse(BigDecimal.ZERO);
    }

    private Set<OrderItem> getOrderItems(Long userId) {
        return shoppingCartRepository.findByUserId(userId)
                .map(shoppingCart -> shoppingCart.getCartItems()
                        .stream()
                        .map(orderItemMapper::cartItemToOrderItem)
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
    }
}
