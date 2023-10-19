package com.varukha.onlinebookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.varukha.onlinebookstore.dto.order.CreateOrderRequestDto;
import com.varukha.onlinebookstore.dto.order.OrderDto;
import com.varukha.onlinebookstore.dto.order.UpdateOrderStatusRequestDto;
import com.varukha.onlinebookstore.dto.orderitem.OrderItemDto;
import com.varukha.onlinebookstore.mapper.OrderItemsMapper;
import com.varukha.onlinebookstore.mapper.OrderMapper;
import com.varukha.onlinebookstore.model.Book;
import com.varukha.onlinebookstore.model.CartItem;
import com.varukha.onlinebookstore.model.Category;
import com.varukha.onlinebookstore.model.Order;
import com.varukha.onlinebookstore.model.OrderItem;
import com.varukha.onlinebookstore.model.ShoppingCart;
import com.varukha.onlinebookstore.model.User;
import com.varukha.onlinebookstore.repository.order.OrderRepository;
import com.varukha.onlinebookstore.repository.orderitem.OrderItemRepository;
import com.varukha.onlinebookstore.repository.shoppingcart.ShoppingCartRepository;
import com.varukha.onlinebookstore.service.order.impl.OrderServiceImpl;
import com.varukha.onlinebookstore.service.shoppingcart.ShoppingCartService;
import com.varukha.onlinebookstore.service.user.UserService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    private static final CreateOrderRequestDto VALID_REQUEST =
            new CreateOrderRequestDto();
    private static final Category BOOK_CATEGORY_1
            = new Category();
    private static final User USER = new User();
    private static final Book VALID_BOOK_1 = new Book();
    private static final Book VALID_BOOK_2 = new Book();
    private static final CartItem CART_ITEM_1 = new CartItem();
    private static final CartItem CART_ITEM_2 = new CartItem();
    private static final ShoppingCart SHOPPING_CART = new ShoppingCart();
    private static final Order VALID_ORDER_1 = new Order();
    private static final Order VALID_ORDER_2 = new Order();
    private static final Order ORDER_TO_UPDATE = new Order();
    private static final OrderDto VALID_ORDER_DTO_1 = new OrderDto();
    private static final OrderDto VALID_ORDER_DTO_2 = new OrderDto();
    private static final OrderDto UPDATED_ORDER_DTO_1 = new OrderDto();
    private static final UpdateOrderStatusRequestDto VALID_UPDATE_STATUS_REQUEST =
            new UpdateOrderStatusRequestDto();
    private static final OrderItem VALID_ORDER_ITEM_1 = new OrderItem();
    private static final OrderItem VALID_ORDER_ITEM_2 = new OrderItem();
    private static final OrderItemDto VALID_ORDER_ITEM_DTO_1 = new OrderItemDto();
    private static final OrderItemDto VALID_ORDER_ITEM_DTO_2 = new OrderItemDto();

    @InjectMocks
    private OrderServiceImpl orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private UserService userService;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private OrderItemsMapper orderItemsMapper;
    @Mock
    private ShoppingCartService shoppingCartService;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @BeforeEach
    void setUp() {
        BOOK_CATEGORY_1.setId(1L);
        BOOK_CATEGORY_1.setName("Science Fiction");
        BOOK_CATEGORY_1.setDescription("Science Fiction category description");

        SHOPPING_CART.setId(1L);
        SHOPPING_CART.setUser(USER);
        SHOPPING_CART.setCartItems(Set.of(CART_ITEM_1));

        USER.setId(1L);
        USER.setEmail("alice@gmail.com");
        USER.setPassword("password");
        USER.setFirstName("Alice");
        USER.setLastName("Brown");
        USER.setShippingAddress("15 Main St, City, Country");

        VALID_BOOK_1.setTitle("Sample Book Title 1");
        VALID_BOOK_1.setAuthor("Author 1");
        VALID_BOOK_1.setIsbn("978-1234567891");
        VALID_BOOK_1.setPrice(BigDecimal.valueOf(150));
        VALID_BOOK_1.setDescription("Description 1");
        VALID_BOOK_1.setCoverImage("https://example.com/book-cover1.jpg");
        VALID_BOOK_1.setCategories(Set.of(BOOK_CATEGORY_1));

        VALID_BOOK_2.setTitle("Sample Book Title 2");
        VALID_BOOK_2.setAuthor("Author 2");
        VALID_BOOK_2.setIsbn("978-1234567892");
        VALID_BOOK_2.setPrice(BigDecimal.valueOf(16));
        VALID_BOOK_2.setDescription("Description 2");
        VALID_BOOK_2.setCoverImage("https://example.com/book-cover2.jpg");
        VALID_BOOK_2.setCategories(Set.of(BOOK_CATEGORY_1));

        CART_ITEM_1.setId(1L);
        CART_ITEM_1.setBook(VALID_BOOK_1);
        CART_ITEM_1.setQuantity(10);
        CART_ITEM_1.setShoppingCart(SHOPPING_CART);

        CART_ITEM_2.setId(2L);
        CART_ITEM_2.setBook(VALID_BOOK_2);
        CART_ITEM_2.setQuantity(5);
        CART_ITEM_2.setShoppingCart(SHOPPING_CART);

        VALID_ORDER_1.setId(1L);
        VALID_ORDER_1.setStatus(Order.Status.PENDING);
        VALID_ORDER_1.setUser(USER);
        VALID_ORDER_1.setTotal(new BigDecimal("100.00"));
        VALID_ORDER_1.setShippingAddress(USER.getShippingAddress());
        VALID_ORDER_1.setOrderDate(
                LocalDateTime.of(2023, 1, 1, 1, 1, 1));
        VALID_ORDER_1.setOrderItems(Set.of(VALID_ORDER_ITEM_1));

        VALID_ORDER_ITEM_1.setId(1L);
        VALID_ORDER_ITEM_1.setBook(VALID_BOOK_1);
        VALID_ORDER_ITEM_1.setOrder(VALID_ORDER_1);
        VALID_ORDER_ITEM_1.setPrice(VALID_BOOK_1.getPrice());
        VALID_ORDER_ITEM_1.setQuantity(10);

        VALID_ORDER_DTO_1.setId(VALID_ORDER_ITEM_1.getId());
        VALID_ORDER_DTO_1.setUserId(VALID_ORDER_1.getUser().getId());
        VALID_ORDER_DTO_1.setOrderDate(String.valueOf(VALID_ORDER_1.getOrderDate()));
        VALID_ORDER_DTO_1.setStatus(VALID_ORDER_1.getStatus().name());
        VALID_ORDER_DTO_1.setTotal(VALID_ORDER_1.getTotal());

        VALID_ORDER_ITEM_DTO_1.setId(VALID_ORDER_ITEM_1.getId());
        VALID_ORDER_ITEM_DTO_1.setBookId(VALID_ORDER_ITEM_1.getBook().getId());
        VALID_ORDER_ITEM_DTO_1.setQuantity(VALID_ORDER_ITEM_1.getQuantity());

        VALID_ORDER_ITEM_2.setId(2L);
        VALID_ORDER_ITEM_2.setBook(VALID_BOOK_2);
        VALID_ORDER_ITEM_2.setOrder(VALID_ORDER_2);
        VALID_ORDER_ITEM_2.setPrice(VALID_BOOK_2.getPrice());
        VALID_ORDER_ITEM_2.setQuantity(5);

        VALID_ORDER_ITEM_DTO_2.setId(VALID_ORDER_ITEM_2.getId());
        VALID_ORDER_ITEM_DTO_2.setBookId(VALID_ORDER_ITEM_2.getBook().getId());
        VALID_ORDER_ITEM_DTO_2.setQuantity(VALID_ORDER_ITEM_2.getQuantity());

        VALID_ORDER_2.setId(2L);
        VALID_ORDER_2.setStatus(Order.Status.PENDING);
        VALID_ORDER_2.setUser(USER);
        VALID_ORDER_2.setTotal(new BigDecimal("200.00"));
        VALID_ORDER_2.setShippingAddress(USER.getShippingAddress());
        VALID_ORDER_2.setOrderDate(
                LocalDateTime.of(2023, 2, 1, 1, 1, 1));
        VALID_ORDER_2.setOrderItems(Set.of(VALID_ORDER_ITEM_1, VALID_ORDER_ITEM_2));

        VALID_ORDER_DTO_2.setId(VALID_ORDER_2.getId());
        VALID_ORDER_DTO_2.setUserId(VALID_ORDER_2.getUser().getId());
        VALID_ORDER_DTO_2.setOrderDate(String.valueOf(VALID_ORDER_2.getOrderDate()));
        VALID_ORDER_DTO_2.setStatus(VALID_ORDER_2.getStatus().name());
        VALID_ORDER_DTO_2.setTotal(VALID_ORDER_2.getTotal());

        VALID_REQUEST.setShippingAddress(USER.getShippingAddress());

        VALID_UPDATE_STATUS_REQUEST.setId(VALID_ORDER_1.getId());
        VALID_UPDATE_STATUS_REQUEST.setStatus(Order.Status.DELIVERED);

        ORDER_TO_UPDATE.setId(1L);
        ORDER_TO_UPDATE.setStatus(Order.Status.PENDING);

        UPDATED_ORDER_DTO_1.setId(ORDER_TO_UPDATE.getId());
        UPDATED_ORDER_DTO_1.setStatus(Order.Status.DELIVERED.name());
    }

    @Test
    @DisplayName("""
            Test the 'create' method to create an order
            with valid request parameters
             """)
    void create_ValidRequestData_ReturnOrderDto() {
        when(userService.getAuthenticatedUser()).thenReturn(USER);
        when(orderMapper.toDto(any())).thenReturn(VALID_ORDER_DTO_1);

        OrderDto actualOrderDto = orderService.create(VALID_REQUEST);

        verify(userService).getAuthenticatedUser();
        verify(shoppingCartService).clearShoppingCart(USER.getId());
        assertNotNull(actualOrderDto);
        assertEquals(VALID_ORDER_DTO_1, actualOrderDto);
    }

    @Test
    @DisplayName("""
            Test the 'getAll' method to retrieve all orders
             with valid request parameters and pagination
            """)
    void getAll_ValidOrderDataRange_ReturnAllOrderDtoList() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Order> orderList = List.of(VALID_ORDER_1, VALID_ORDER_2);

        when(userService.getAuthenticatedUser()).thenReturn(USER);
        when(orderRepository.findAllByUserId(pageable, USER.getId())).thenReturn(orderList);
        when(orderMapper.toDto(VALID_ORDER_1)).thenReturn(VALID_ORDER_DTO_1);
        when(orderMapper.toDto(VALID_ORDER_2)).thenReturn(VALID_ORDER_DTO_2);

        List<OrderDto> actualOrderDtoList = orderService.getAll(pageable);

        assertNotNull(actualOrderDtoList);
        assertThat(actualOrderDtoList).hasSize(2);
        assertEquals(VALID_ORDER_DTO_1, actualOrderDtoList.get(0));
        assertEquals(VALID_ORDER_DTO_2, actualOrderDtoList.get(1));

        assertTrue(actualOrderDtoList.contains(VALID_ORDER_DTO_1)
                && actualOrderDtoList.contains(VALID_ORDER_DTO_2));
        verify(orderRepository).findAllByUserId(pageable, USER.getId());
        verifyNoMoreInteractions(orderRepository, orderMapper);
    }

    @Test
    @DisplayName("""
            Test the 'getAllOrderItemsByOrderId' method to retrieve all order item
            by valid order ID
            """)
    void getAllOrderItemsByOrderId_ValidOrderItemsId_ReturnSetOfOrderItemDto() {
        Long orderId = VALID_ORDER_2.getId();
        when(orderRepository.findOrderById(orderId))
                .thenReturn(Optional.of(VALID_ORDER_2));
        when(orderItemsMapper.toDto(VALID_ORDER_ITEM_1))
                .thenReturn(VALID_ORDER_ITEM_DTO_1);
        when(orderItemsMapper.toDto(VALID_ORDER_ITEM_2))
                .thenReturn(VALID_ORDER_ITEM_DTO_2);

        Set<OrderItemDto> actualOrderItemDtoSet = orderService.getAllOrderItemsByOrderId(orderId);

        assertNotNull(actualOrderItemDtoSet);
        assertTrue(actualOrderItemDtoSet.contains(VALID_ORDER_ITEM_DTO_1)
                && actualOrderItemDtoSet.contains(VALID_ORDER_ITEM_DTO_2));
        verify(orderRepository).findOrderById(orderId);
        verifyNoMoreInteractions(orderRepository, orderMapper);
    }

    @Test
    @DisplayName("""
            Test the 'getOrderItemFromOrderById' method to retrieve a order item
            from order by order ID and order item ID with valid IDs
            """)
    void getOrderItemFromOrderById_ValidOrderIdAndOrderItemId_ReturnOrderItemDto() {
        Long orderItemId = VALID_ORDER_ITEM_2.getId();
        Long orderId = VALID_ORDER_2.getId();
        when(orderItemRepository.findOrderItemByIdAndOrderId(orderItemId, orderId))
                .thenReturn(Optional.of(VALID_ORDER_ITEM_2));
        when(orderItemsMapper.toDto(VALID_ORDER_ITEM_2))
                .thenReturn(VALID_ORDER_ITEM_DTO_2);

        OrderItemDto actualOrderItemDto = orderService
                .getOrderItemFromOrderById(orderItemId, orderId);

        assertNotNull(actualOrderItemDto);
        assertEquals(VALID_ORDER_ITEM_DTO_2, actualOrderItemDto);
        verify(orderItemRepository).findOrderItemByIdAndOrderId(orderItemId, orderId);
        verifyNoMoreInteractions(orderRepository, orderMapper);
    }

    @Test
    @DisplayName("""
            Test the 'updateOrderStatus' method to update order status
            with valid order ID and new order status
            """)
    void updateOrderStatus_ValidRequestData_ReturnUpdatedOrderDto() {
        Long orderId = ORDER_TO_UPDATE.getId();
        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(ORDER_TO_UPDATE));
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        when(orderRepository.save(orderCaptor.capture()))
                .thenAnswer(invocation -> {
                    Order updatedOrder = invocation.getArgument(0);
                    ORDER_TO_UPDATE.setStatus(updatedOrder.getStatus());
                    return updatedOrder;
                });
        when(orderMapper.toDto(ORDER_TO_UPDATE)).thenReturn(UPDATED_ORDER_DTO_1);

        OrderDto actualOrderDto = orderService.updateOrderStatus(
                orderId,
                VALID_UPDATE_STATUS_REQUEST);

        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(ORDER_TO_UPDATE);
        assertEquals(UPDATED_ORDER_DTO_1, actualOrderDto);
        assertEquals(UPDATED_ORDER_DTO_1.getStatus(), actualOrderDto.getStatus());

    }
}
