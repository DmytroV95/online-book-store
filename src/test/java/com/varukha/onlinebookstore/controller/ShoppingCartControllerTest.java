package com.varukha.onlinebookstore.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.varukha.onlinebookstore.dto.cartitem.CartItemDto;
import com.varukha.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.varukha.onlinebookstore.dto.cartitem.UpdateCartItemRequestDto;
import com.varukha.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.varukha.onlinebookstore.model.Book;
import com.varukha.onlinebookstore.model.CartItem;
import com.varukha.onlinebookstore.model.Category;
import com.varukha.onlinebookstore.model.ShoppingCart;
import com.varukha.onlinebookstore.model.User;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
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
class ShoppingCartControllerTest {
    protected static MockMvc mockMvc;

    private static final User USER = new User();
    private static final CartItem CART_ITEM = new CartItem();
    private static final Book BOOK = new Book();
    private static final Category VALID_CATEGORY_1 = new Category();
    private static final ShoppingCart SHOPPING_CART = new ShoppingCart();
    private static final CreateCartItemRequestDto VALID_CART_ITEM_REQUEST
            = new CreateCartItemRequestDto();
    private static final CartItemDto VALID_CART_ITEM_RESPONSE
            = new CartItemDto();
    private static final ShoppingCartDto VALID_SHOPPING_CART_DTO
            = new ShoppingCartDto();
    private static final UpdateCartItemRequestDto VALID_UPDATE_REQUEST
            = new UpdateCartItemRequestDto();
    private static final CartItemDto VALID_UPDATE_RESPONSE
            = new CartItemDto();

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
                    new ClassPathResource(
                            "database/shoppingcart/set-up-cart-item-and-shopping-cart.sql")
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
                    new ClassPathResource(
                            "database/shoppingcart/remove-shopping-cart-and-cart-item.sql")
            );
        }
    }

    @BeforeEach
    void setUp() {
        USER.setId(1L);
        USER.setEmail("alice@gmail.com");
        USER.setPassword("password");
        USER.setFirstName("Alice");
        USER.setLastName("Brown");
        USER.setShippingAddress("15 Main St, City, Country");
        USER.setDeleted(false);

        VALID_CATEGORY_1.setId(1L);
        VALID_CATEGORY_1.setName("Science Fiction");
        VALID_CATEGORY_1.setDescription("Science Fiction category description");

        BOOK.setTitle("Sample Book Title 1");
        BOOK.setAuthor("Author 1");
        BOOK.setIsbn("978-1234567891");
        BOOK.setPrice(BigDecimal.valueOf(25));
        BOOK.setDescription("Description 1");
        BOOK.setCoverImage("https://example.com/book-cover1.jpg");
        BOOK.setCategories(Set.of(VALID_CATEGORY_1));

        VALID_CART_ITEM_REQUEST.setBookId(1L);
        VALID_CART_ITEM_REQUEST.setQuantity(1);

        VALID_CART_ITEM_RESPONSE.setId(1L);
        VALID_CART_ITEM_RESPONSE.setBookId(BOOK.getId());
        VALID_CART_ITEM_RESPONSE.setQuantity(VALID_CART_ITEM_REQUEST.getQuantity());
        VALID_CART_ITEM_RESPONSE.setBookTitle(BOOK.getTitle());

        CART_ITEM.setId(1L);
        CART_ITEM.setShoppingCart(SHOPPING_CART);
        CART_ITEM.setBook(BOOK);
        CART_ITEM.setQuantity(1);

        SHOPPING_CART.setId(1L);
        SHOPPING_CART.setUser(USER);
        SHOPPING_CART.setCartItems(Set.of());

        VALID_SHOPPING_CART_DTO.setUserId(1L);
        VALID_SHOPPING_CART_DTO.setCartItems(Set.of(VALID_CART_ITEM_RESPONSE));

        VALID_UPDATE_REQUEST.setQuantity(5);

        VALID_UPDATE_RESPONSE.setId(VALID_CART_ITEM_RESPONSE.getId());
        VALID_UPDATE_RESPONSE.setBookId(VALID_CART_ITEM_RESPONSE.getBookId());
        VALID_UPDATE_RESPONSE.setQuantity(VALID_UPDATE_REQUEST.getQuantity());
        VALID_UPDATE_RESPONSE.setBookTitle(VALID_CART_ITEM_RESPONSE.getBookTitle());
    }

    @Test
    @WithMockUser(username = "alice@gmail.com")
    @DisplayName("""
            Test the 'saveCartItem' endpoint for saving new cart item
            to user shopping cart with valid request parameters
             """)
    void saveCartItem_ValidRequestData_ReturnCartItemDto() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(VALID_CART_ITEM_REQUEST);
        MvcResult result = mockMvc.perform(post("/carts")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        CartItemDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CartItemDto.class
        );
        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(
                VALID_CART_ITEM_RESPONSE,
                actual);
    }

//    @Test
//    @WithMockUser(username = "alice@gmail.com")
//    @DisplayName("""
//            Test the 'getShoppingCartById' endpoint in order to get
//            user shopping cart by valid shopping cart ID
//             """)
//    void getShoppingCart_ReturnShoppingCartDto() throws Exception {
//        MvcResult result = mockMvc.perform(get("/carts")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn();
//        ShoppingCartDto actualShoppingCartDto = objectMapper.readValue(
//                result.getResponse().getContentAsString(),
//                ShoppingCartDto.class
//        );
//        assertNotNull(actualShoppingCartDto);
//        assertNotNull(actualShoppingCartDto.getId());
//        assertEquals(SHOPPING_CART.getId(), actualShoppingCartDto.getId());
//        EqualsBuilder.reflectionEquals(SHOPPING_CART, actualShoppingCartDto);
//    }

    @Test
    @WithMockUser(username = "alice@gmail.com")
    @DisplayName("""
            Test the 'deleteCartItemById' endpoint to delete
            cart item from shopping cart by valid cart item ID
             """)
    void deleteCartItemById() throws Exception {
        mockMvc.perform(delete("/carts/cart-items/" + CART_ITEM.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "alice@gmail.com")
    @DisplayName("""
            Test the 'update' endpoint to update
            cart item quantity in shopping cart by valid cart item ID
             """)
    void update_ValidRequestData_ReturnCartItemDto() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(VALID_UPDATE_REQUEST);
        MvcResult result = mockMvc.perform(put(
                        "/carts/cart-items/" + CART_ITEM.getId())
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CartItemDto actualBookDto = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CartItemDto.class);
        assertNotNull(actualBookDto);
        assertEquals(VALID_UPDATE_RESPONSE.getQuantity(), actualBookDto.getQuantity());
        EqualsBuilder.reflectionEquals(VALID_UPDATE_RESPONSE, actualBookDto);
    }
}
