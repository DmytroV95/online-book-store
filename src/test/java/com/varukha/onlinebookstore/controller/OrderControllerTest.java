package com.varukha.onlinebookstore.controller;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.varukha.onlinebookstore.dto.order.CreateOrderRequestDto;
import com.varukha.onlinebookstore.dto.order.OrderDto;
import com.varukha.onlinebookstore.dto.order.UpdateOrderStatusRequestDto;
import com.varukha.onlinebookstore.dto.orderitem.OrderItemDto;
import com.varukha.onlinebookstore.model.Book;
import com.varukha.onlinebookstore.model.Category;
import com.varukha.onlinebookstore.model.Order;
import com.varukha.onlinebookstore.model.OrderItem;
import com.varukha.onlinebookstore.model.User;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerTest {
    protected static MockMvc mockMvc;

    private static final String SQL_SCRIPT_BEFORE_TEST_METHOD_EXECUTION_ADD_DATA =
            "database/order/add-order-to-order-table.sql";
    private static final String SQL_SCRIPT_AFTER_TEST_METHOD_EXECUTION_REMOVE_DATA =
            "database/remove-all-from-database-tables.sql";

    private static final CreateOrderRequestDto CREATE_ORDER_REQUEST_DTO =
            new CreateOrderRequestDto();
    private static final Category BOOK_CATEGORY_1 = new Category();
    private static final Category BOOK_CATEGORY_2 = new Category();
    private static final User USER = new User();
    private static final Book VALID_BOOK_1 = new Book();
    private static final Book VALID_BOOK_2 = new Book();
    private static final Order VALID_ORDER_1 = new Order();
    private static final Order VALID_ORDER_2 = new Order();
    private static final Order NEW_VALID_ORDER_3 = new Order();
    private static final OrderItem VALID_ORDER_ITEM_1 = new OrderItem();
    private static final OrderItem VALID_ORDER_ITEM_2 = new OrderItem();
    private static final OrderItem VALID_ORDER_ITEM_3 = new OrderItem();
    private static final OrderDto VALID_ORDER_DTO_1 = new OrderDto();
    private static final OrderDto VALID_ORDER_DTO_2 = new OrderDto();
    private static final OrderDto NEW_ORDER_DTO_3 = new OrderDto();
    private static final OrderItemDto VALID_ORDER_ITEM_DTO_1 = new OrderItemDto();
    private static final OrderItemDto VALID_ORDER_ITEM_DTO_2 = new OrderItemDto();
    private static final OrderItemDto VALID_ORDER_ITEM_DTO_3 = new OrderItemDto();
    private static final UpdateOrderStatusRequestDto UPDATE_ORDER_STATUS_REQUEST_DTO =
            new UpdateOrderStatusRequestDto();
    private static final OrderDto UPDATED_ORDER_DTO_2_STATUS = new OrderDto();

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(SQL_SCRIPT_BEFORE_TEST_METHOD_EXECUTION_ADD_DATA)
            );
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(SQL_SCRIPT_AFTER_TEST_METHOD_EXECUTION_REMOVE_DATA)
            );
        }
    }

    @BeforeEach
    void setUp() {
        BOOK_CATEGORY_1.setId(1L);
        BOOK_CATEGORY_1.setName("Science Fiction");
        BOOK_CATEGORY_1.setDescription("Science Fiction category description");

        BOOK_CATEGORY_2.setId(2L);
        BOOK_CATEGORY_2.setName("Mystery");
        BOOK_CATEGORY_2.setDescription("Mystery category description");

        USER.setId(1L);
        USER.setEmail("alice@gmail.com");
        USER.setPassword("password");
        USER.setFirstName("Alice");
        USER.setLastName("Brown");
        USER.setShippingAddress("15 Main St, City, Country");

        CREATE_ORDER_REQUEST_DTO.setShippingAddress(USER.getShippingAddress());

        VALID_BOOK_1.setId(1L);
        VALID_BOOK_1.setTitle("Sample Book Title 1");
        VALID_BOOK_1.setAuthor("Author 1");
        VALID_BOOK_1.setIsbn("978-1234567891");
        VALID_BOOK_1.setPrice(BigDecimal.valueOf(150));
        VALID_BOOK_1.setDescription("Description 1");
        VALID_BOOK_1.setCoverImage("https://example.com/book-cover1.jpg");
        VALID_BOOK_1.setCategories(Set.of(BOOK_CATEGORY_1));

        VALID_BOOK_2.setId(2L);
        VALID_BOOK_2.setTitle("Sample Book Title 2");
        VALID_BOOK_2.setAuthor("Author 2");
        VALID_BOOK_2.setIsbn("978-1234567892");
        VALID_BOOK_2.setPrice(BigDecimal.valueOf(150));
        VALID_BOOK_2.setDescription("Description 2");
        VALID_BOOK_2.setCoverImage("https://example.com/book-cover2.jpg");
        VALID_BOOK_2.setCategories(Set.of(BOOK_CATEGORY_2));

        VALID_ORDER_ITEM_1.setId(1L);
        VALID_ORDER_ITEM_1.setBook(VALID_BOOK_1);
        VALID_ORDER_ITEM_1.setOrder(VALID_ORDER_1);
        VALID_ORDER_ITEM_1.setPrice(VALID_BOOK_1.getPrice());
        VALID_ORDER_ITEM_1.setQuantity(5);

        VALID_ORDER_1.setId(1L);
        VALID_ORDER_1.setStatus(Order.Status.PENDING);
        VALID_ORDER_1.setUser(USER);
        VALID_ORDER_1.setTotal(new BigDecimal(150));
        VALID_ORDER_1.setShippingAddress(USER.getShippingAddress());
        VALID_ORDER_1.setOrderDate(
                LocalDateTime.of(2023, 10, 18, 23, 39, 38));
        VALID_ORDER_1.setOrderItems(Set.of(VALID_ORDER_ITEM_1));

        VALID_ORDER_DTO_1.setId(VALID_ORDER_ITEM_1.getId());
        VALID_ORDER_DTO_1.setUserId(VALID_ORDER_1.getUser().getId());
        VALID_ORDER_DTO_1.setOrderDate(String.valueOf(VALID_ORDER_1.getOrderDate()));
        VALID_ORDER_DTO_1.setOrderItems(Set.of(VALID_ORDER_ITEM_DTO_1));
        VALID_ORDER_DTO_1.setStatus(VALID_ORDER_1.getStatus().name());
        VALID_ORDER_DTO_1.setTotal(VALID_ORDER_1.getTotal());

        VALID_ORDER_ITEM_DTO_1.setId(VALID_ORDER_ITEM_1.getId());
        VALID_ORDER_ITEM_DTO_1.setBookId(VALID_ORDER_ITEM_1.getBook().getId());
        VALID_ORDER_ITEM_DTO_1.setQuantity(VALID_ORDER_ITEM_1.getQuantity());

        VALID_ORDER_ITEM_2.setId(2L);
        VALID_ORDER_ITEM_2.setBook(VALID_BOOK_2);
        VALID_ORDER_ITEM_2.setOrder(VALID_ORDER_2);
        VALID_ORDER_ITEM_2.setPrice(VALID_BOOK_2.getPrice());
        VALID_ORDER_ITEM_2.setQuantity(10);

        VALID_ORDER_ITEM_DTO_2.setId(VALID_ORDER_ITEM_2.getId());
        VALID_ORDER_ITEM_DTO_2.setBookId(VALID_ORDER_ITEM_2.getBook().getId());
        VALID_ORDER_ITEM_DTO_2.setQuantity(VALID_ORDER_ITEM_2.getQuantity());

        VALID_ORDER_ITEM_3.setId(3L);
        VALID_ORDER_ITEM_3.setBook(VALID_BOOK_1);
        VALID_ORDER_ITEM_3.setOrder(VALID_ORDER_1);
        VALID_ORDER_ITEM_3.setPrice(VALID_BOOK_1.getPrice());
        VALID_ORDER_ITEM_3.setQuantity(15);

        VALID_ORDER_ITEM_DTO_3.setId(VALID_ORDER_ITEM_3.getId());
        VALID_ORDER_ITEM_DTO_3.setBookId(VALID_ORDER_ITEM_3.getBook().getId());
        VALID_ORDER_ITEM_DTO_3.setQuantity(VALID_ORDER_ITEM_3.getQuantity());

        VALID_ORDER_2.setId(2L);
        VALID_ORDER_2.setStatus(Order.Status.PENDING);
        VALID_ORDER_2.setUser(USER);
        VALID_ORDER_2.setTotal(new BigDecimal(150));
        VALID_ORDER_2.setShippingAddress(USER.getShippingAddress());
        VALID_ORDER_2.setOrderDate(
                LocalDateTime.of(2023, 10, 18, 23, 39, 39));
        VALID_ORDER_2.setOrderItems(Set.of(VALID_ORDER_ITEM_2, VALID_ORDER_ITEM_3));

        VALID_ORDER_DTO_2.setId(VALID_ORDER_2.getId());
        VALID_ORDER_DTO_2.setUserId(VALID_ORDER_2.getUser().getId());
        VALID_ORDER_DTO_2.setOrderDate(String.valueOf(VALID_ORDER_2.getOrderDate()));
        VALID_ORDER_DTO_2.setOrderItems(Set.of(VALID_ORDER_ITEM_DTO_2, VALID_ORDER_ITEM_DTO_3));
        VALID_ORDER_DTO_2.setStatus(VALID_ORDER_2.getStatus().name());
        VALID_ORDER_DTO_2.setTotal(VALID_ORDER_2.getTotal());

        NEW_VALID_ORDER_3.setId(3L);
        NEW_VALID_ORDER_3.setStatus(Order.Status.PENDING);
        NEW_VALID_ORDER_3.setUser(USER);
        NEW_VALID_ORDER_3.setTotal(new BigDecimal(2250));
        NEW_VALID_ORDER_3.setShippingAddress(USER.getShippingAddress());
        NEW_VALID_ORDER_3.setOrderDate(
                LocalDateTime.of(2023, 10, 18, 23, 39, 39));
        NEW_VALID_ORDER_3.setOrderItems(Set.of(VALID_ORDER_ITEM_2, VALID_ORDER_ITEM_3));

        NEW_ORDER_DTO_3.setId(NEW_VALID_ORDER_3.getId());
        NEW_ORDER_DTO_3.setUserId(NEW_VALID_ORDER_3.getUser().getId());
        NEW_ORDER_DTO_3.setOrderDate(String.valueOf(NEW_VALID_ORDER_3.getOrderDate()));
        NEW_ORDER_DTO_3.setOrderItems(Set.of(VALID_ORDER_ITEM_DTO_1, VALID_ORDER_ITEM_DTO_2));
        NEW_ORDER_DTO_3.setStatus(NEW_VALID_ORDER_3.getStatus().name());
        NEW_ORDER_DTO_3.setTotal(NEW_VALID_ORDER_3.getTotal());

        UPDATE_ORDER_STATUS_REQUEST_DTO.setStatus(Order.Status.DELIVERED);

        UPDATED_ORDER_DTO_2_STATUS.setStatus(UPDATE_ORDER_STATUS_REQUEST_DTO.getStatus().name());
    }

    @Test
    @WithMockUser(username = "alice@gmail.com")
    @DisplayName("""
            Test the 'create' endpoint to place new order with
            valid shipping address
            """)
    void create_ValidShippingAddress_ReturnOrderDto() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(CREATE_ORDER_REQUEST_DTO);
        MvcResult result = mockMvc.perform(post("/orders")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        OrderDto actualOrderDto = objectMapper.readValue(
                result.getResponse()
                .getContentAsString(), OrderDto.class);

        assertNotNull(actualOrderDto);
        assertNotNull(actualOrderDto.getId());
        assertTrue(reflectionEquals(NEW_ORDER_DTO_3,
                actualOrderDto,
                "orderDate"));
    }

    @Test
    @WithMockUser(username = "alice@gmail.com")
    @DisplayName("""
            Test the 'getAll' endpoint to retrieve all user orders
            """)
    void getAll_PageableArgument_ReturnListOfOrderDto() throws Exception {
        MvcResult result = mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<OrderDto> actualOrderDto = objectMapper.readValue(result.getResponse()
                        .getContentAsString(),
                objectMapper.getTypeFactory()
                        .constructCollectionType(List.class, OrderDto.class));

        assertNotNull(actualOrderDto);
        assertEquals(2, actualOrderDto.size());
        assertEquals(VALID_ORDER_DTO_1, actualOrderDto.get(0));
        assertEquals(VALID_ORDER_DTO_2, actualOrderDto.get(1));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("""
            Test the 'getAllOrderItemsByOrderId' endpoint to retrieve all order item
            by valid order ID
            """)
    void getAllOrderItemsByOrderId_ValidOrderItemsId_ReturnOrderItemDto() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/orders/" + VALID_ORDER_2.getId() + "/items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<OrderItemDto> actualOrderItems = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory()
                        .constructCollectionType(List.class, OrderItemDto.class));

        assertNotNull(actualOrderItems);
        assertEquals(2, actualOrderItems.size());
        assertEquals(VALID_ORDER_ITEM_DTO_2.getId(), actualOrderItems.get(0).getId());
        assertEquals(VALID_ORDER_ITEM_DTO_3.getId(), actualOrderItems.get(1).getId());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("""
            Test the 'getOrderItemFromOrderById' endpoint to retrieve a order item
            from order by order ID and order item ID
            """)
    void getOrderItemFromOrderById_ValidOrderIdAndOrderItemId_ReturnOrderItemDto()
            throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/orders/"
                                + VALID_ORDER_2.getId()
                                + "/items/"
                                + VALID_ORDER_ITEM_3.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        OrderItemDto actualOrderItemDto = objectMapper.readValue(result.getResponse()
                .getContentAsString(), OrderItemDto.class);

        assertNotNull(actualOrderItemDto);
        assertNotNull(actualOrderItemDto.getId());
        assertEquals(VALID_ORDER_ITEM_DTO_3.getId(), actualOrderItemDto.getId());
        assertTrue(reflectionEquals(VALID_ORDER_ITEM_DTO_3, actualOrderItemDto));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("""
            Test the 'updateOrderStatus' endpoint to update order status
            with valid order ID and new order status
            """)
    void updateOrderStatus_ValidRequestData_ReturnUpdatedOrderDto() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(UPDATE_ORDER_STATUS_REQUEST_DTO);
        MvcResult result = mockMvc.perform(patch("/orders/" + VALID_ORDER_2.getId())
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        OrderDto actualOrderDto = objectMapper.readValue(result.getResponse()
                .getContentAsString(), OrderDto.class);

        assertNotNull(actualOrderDto);
        assertEquals(UPDATED_ORDER_DTO_2_STATUS.getStatus(), actualOrderDto.getStatus());
    }
}
