package com.varukha.onlinebookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.varukha.onlinebookstore.model.ShoppingCart;
import com.varukha.onlinebookstore.model.User;
import com.varukha.onlinebookstore.repository.shoppingcart.ShoppingCartRepository;
import java.sql.Connection;
import java.util.Optional;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ShoppingCartRepositoryTest {
    private static final String SQL_SCRIPT_BEFORE_TEST_METHOD_EXECUTION_CREATE_SHOPPING_CART =
            "classpath:database/shoppingcart/create-shopping-cart-for-user.sql";
    private static final String SQL_SCRIPT_AFTER_TEST_METHOD_EXECUTION_REMOVE_SHOPPING_CART =
            "classpath:database/shoppingcart/remove-user-shopping-cart.sql";
    private static final String SQL_SCRIPT_BEFORE_TEST_METHOD_EXECUTION_CLEAR_DATABASE =
            "database/remove-all-from-database-tables.sql";
    private static final User USER = new User();
    private static final ShoppingCart SHOPPING_CART_RESPONSE = new ShoppingCart();

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @BeforeAll
    static void beforeAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(SQL_SCRIPT_BEFORE_TEST_METHOD_EXECUTION_CLEAR_DATABASE)
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

        SHOPPING_CART_RESPONSE.setId(1L);
        SHOPPING_CART_RESPONSE.setUser(USER);
    }

    @Test
    @DisplayName("""
            Test 'findByUserId' method to get shopping cart by user ID
            """)
    @Sql(scripts = SQL_SCRIPT_BEFORE_TEST_METHOD_EXECUTION_CREATE_SHOPPING_CART,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = SQL_SCRIPT_AFTER_TEST_METHOD_EXECUTION_REMOVE_SHOPPING_CART,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByUserId_ValidUserId_ReturnOptionalShoppingCArt() {
        Optional<ShoppingCart> actualShoppingCart = shoppingCartRepository
                .findByUserId(USER.getId());
        assertTrue(actualShoppingCart.isPresent(),
                "Expected shopping cart by user id: "
                + USER.getId() + " is present");
        assertEquals(SHOPPING_CART_RESPONSE, actualShoppingCart.get());
    }
}
