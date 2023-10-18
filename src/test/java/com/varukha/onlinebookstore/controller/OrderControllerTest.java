package com.varukha.onlinebookstore.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.varukha.onlinebookstore.dto.book.response.BookDto;
import com.varukha.onlinebookstore.dto.order.CreateOrderRequestDto;
import com.varukha.onlinebookstore.dto.order.OrderDto;
import com.varukha.onlinebookstore.dto.orderitem.OrderItemDto;
import com.varukha.onlinebookstore.model.Book;
import com.varukha.onlinebookstore.model.Category;
import com.varukha.onlinebookstore.model.Order;
import com.varukha.onlinebookstore.model.OrderItem;
import com.varukha.onlinebookstore.model.User;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.sql.Connection;
import java.sql.SQLException;
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
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerTest {
    protected static MockMvc mockMvc;

    private static final String SQL_SCRIPT_BEFORE_TEST_METHOD_EXECUTION_ADD_DATA =
            "database/order/add-order-to-order-table.sql";
    private static final String SQL_SCRIPT_AFTER_TEST_METHOD_EXECUTION_REMOVE_DATA =
            "database/order/remove-order-from-order-table.sql";

    private static final CreateOrderRequestDto VALID_REQUEST =
            new CreateOrderRequestDto();
    private static final Category BOOK_CATEGORY_1 = new Category();
    private static final Category BOOK_CATEGORY_2 = new Category();
    private static final User USER = new User();
    private static final Book VALID_BOOK_1 = new Book();
    private static final Book VALID_BOOK_2 = new Book();
    private static final Order VALID_ORDER_1 = new Order();
    private static final Order VALID_ORDER_2 = new Order();
    private static final OrderItem VALID_ORDER_ITEM_1 = new OrderItem();
    private static final OrderItem VALID_ORDER_ITEM_2 = new OrderItem();
    private static final OrderDto VALID_ORDER_DTO_1 = new OrderDto();
    private static final OrderDto VALID_ORDER_DTO_2 = new OrderDto();
    private static final OrderItemDto VALID_ORDER_ITEM_DTO_1 = new OrderItemDto();
    private static final OrderItemDto VALID_ORDER_ITEM_DTO_2 = new OrderItemDto();

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

        VALID_REQUEST.setShippingAddress("15 Main St, City, Country");

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

        VALID_ORDER_2.setId(2L);
        VALID_ORDER_2.setStatus(Order.Status.PENDING);
        VALID_ORDER_2.setUser(USER);
        VALID_ORDER_2.setTotal(new BigDecimal(150));
        VALID_ORDER_2.setShippingAddress(USER.getShippingAddress());
        VALID_ORDER_2.setOrderDate(
                LocalDateTime.of(2023, 10, 18, 23, 39, 39));
        VALID_ORDER_2.setOrderItems(Set.of(VALID_ORDER_ITEM_2));

        VALID_ORDER_DTO_2.setId(VALID_ORDER_2.getId());
        VALID_ORDER_DTO_2.setUserId(VALID_ORDER_2.getUser().getId());
        VALID_ORDER_DTO_2.setOrderDate(String.valueOf(VALID_ORDER_2.getOrderDate()));
        VALID_ORDER_DTO_2.setOrderItems(Set.of(VALID_ORDER_ITEM_DTO_2));
        VALID_ORDER_DTO_2.setStatus(VALID_ORDER_2.getStatus().name());
        VALID_ORDER_DTO_2.setTotal(VALID_ORDER_2.getTotal());
    }

    @Test
    @WithMockUser(roles = "USER")
//    @WithMockUser(username = "alice@gmail.com")
    @DisplayName("""
            Test the 'create' endpoint to place new order with
            valid request parameters
            """)
    void create() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(VALID_REQUEST);
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
        assertTrue(EqualsBuilder.reflectionEquals(
                VALID_ORDER_DTO_1,
                actualOrderDto)
        );
    }

    @Test
    @WithMockUser(roles = "USER")
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
                objectMapper.getTypeFactory().constructCollectionType(List.class,
                        OrderDto.class));
        assertNotNull(actualOrderDto);
        assertEquals(2, actualOrderDto.size());
        assertEquals(VALID_ORDER_DTO_1, actualOrderDto.get(0));
        assertEquals(VALID_ORDER_DTO_2, actualOrderDto.get(1));
    }

    @Test
    void getAllOrderItemsByOrderId() {
    }

    @Test
    void getOrderItemFromOrderById() {
    }

    @Test
    void updateOrderStatus() {
    }
}