package com.varukha.onlinebookstore.service.order;

import com.varukha.onlinebookstore.dto.order.CreateOrderRequestDto;
import com.varukha.onlinebookstore.dto.order.OrderDto;
import com.varukha.onlinebookstore.dto.order.UpdateOrderStatusRequestDto;
import com.varukha.onlinebookstore.dto.orderitem.OrderItemDto;
import com.varukha.onlinebookstore.exception.EntityNotFoundException;
import com.varukha.onlinebookstore.mapper.OrderItemsMapper;
import com.varukha.onlinebookstore.mapper.OrderMapper;
import com.varukha.onlinebookstore.model.Order;
import com.varukha.onlinebookstore.model.OrderItem;
import com.varukha.onlinebookstore.model.User;
import com.varukha.onlinebookstore.repository.order.OrderRepository;
import com.varukha.onlinebookstore.repository.orderitem.OrderItemRepository;
import com.varukha.onlinebookstore.repository.shoppingcart.ShoppingCartRepository;
import com.varukha.onlinebookstore.service.shoppingcart.ShoppingCartService;
import com.varukha.onlinebookstore.service.user.UserService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserService userService;
    private final OrderMapper orderMapper;
    private final OrderItemsMapper orderItemsMapper;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ShoppingCartService shoppingCartService;
    private final ShoppingCartRepository shoppingCartRepository;

    @Transactional
    @Override
    public OrderDto create(CreateOrderRequestDto orderRequestDto) {
        User authenticatedUser = userService.getAuthenticatedUser();
        Long userId = authenticatedUser.getId();
        Set<OrderItem> orderItems = getOrderItems(userId);
        Order order = createOrder(authenticatedUser, orderRequestDto, orderItems);
        shoppingCartService.clearShoppingCart(userId);
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderDto> getAll(Pageable pageable) {
        User authenticatedUser = userService.getAuthenticatedUser();
        return orderRepository.findAllByUserId(pageable, authenticatedUser.getId())
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public Set<OrderItemDto> getAllOrderItemsByOrderId(Long id) {
        return orderRepository.findOrderById(id)
                .map(orderItem -> orderItem.getOrderItems()
                        .stream()
                        .map(orderItemsMapper::toDto)
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
    }

    @Override
    public OrderItemDto getOrderItemFromOrderById(Long orderId, Long itemId) {
        return orderItemRepository.findOrderItemByIdAndOrderId(itemId, orderId)
                .map(orderItemsMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Order item by item id: "
                        + itemId + " and order id: " + orderId + " not found"));
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

    private Order createOrder(User authenticatedUser,
                              CreateOrderRequestDto orderRequestDto,
                              Set<OrderItem> orderItems) {
        Order order = new Order();
        order.setUser(authenticatedUser);
        order.setStatus(Order.Status.PENDING);
        order.setTotal(calculateTotalCartItemsPrice(authenticatedUser.getId()));
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(orderRequestDto.getShippingAddress());
        order.setOrderItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        orderItems.forEach(orderItem -> orderItem.setOrder(savedOrder));
        orderItemRepository.saveAll(orderItems);
        return savedOrder;
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
                        .map(orderItemsMapper::toModel)
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
    }
}
