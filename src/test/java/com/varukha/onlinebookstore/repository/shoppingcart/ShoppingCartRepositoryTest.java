package com.varukha.onlinebookstore.repository.shoppingcart;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.varukha.onlinebookstore.model.ShoppingCart;
import com.varukha.onlinebookstore.model.User;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ShoppingCartRepositoryTest {
    private static final String SQL_SCRIPT_BEFORE_TEST_METHOD_EXECUTION_CREATE_SHOPPING_CART =
            "classpath:database/shoppingcart/create-shopping-cart-for-user.sql";
    private static final String SQL_SCRIPT_AFTER_TEST_METHOD_EXECUTION_REMOVE_SHOPPING_CART =
            "classpath:database/shoppingcart/remove-user-shopping-cart.sql";

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @DisplayName("""
            Test 'findByUserId' method to get shopping cart by user ID
            """)
    @Sql(scripts = SQL_SCRIPT_BEFORE_TEST_METHOD_EXECUTION_CREATE_SHOPPING_CART,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = SQL_SCRIPT_AFTER_TEST_METHOD_EXECUTION_REMOVE_SHOPPING_CART,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByUserId_ValidUserId_ReturnOptionalShoppingCArt() {
        User user = new User();
        user.setId(1L);

        ShoppingCart expected = new ShoppingCart();
        expected.setId(1L);
        expected.setUser(user);

        Optional<ShoppingCart> actual = shoppingCartRepository.findByUserId(user.getId());
        assertTrue(actual.isPresent(), "Expected shopping cart by user id: "
                + user.getId() + " is present");
        EqualsBuilder.reflectionEquals(expected, actual.get());
    }
}
