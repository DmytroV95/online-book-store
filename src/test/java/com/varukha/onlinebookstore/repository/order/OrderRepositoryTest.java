package com.varukha.onlinebookstore.repository.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.varukha.onlinebookstore.model.Book;
import com.varukha.onlinebookstore.model.Category;
import com.varukha.onlinebookstore.model.Order;
import com.varukha.onlinebookstore.model.OrderItem;
import com.varukha.onlinebookstore.model.User;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderRepositoryTest {
    private static final String SQL_SCRIPT_BEFORE_TEST_METHOD_EXECUTION_ADD_DATA =
            "classpath:database/order/add-order-to-order-table.sql";
    private static final String SQL_SCRIPT_AFTER_TEST_METHOD_EXECUTION_REMOVE_DATA =
            "classpath:database/order/remove-order-from-order-table.sql";
    private static final Category BOOK_CATEGORY_1 = new Category();
    private static final Category BOOK_CATEGORY_2 = new Category();
    private static final User USER = new User();
    private static final Book VALID_BOOK_1 = new Book();
    private static final Book VALID_BOOK_2 = new Book();
    private static final Order VALID_ORDER_1 = new Order();
    private static final Order VALID_ORDER_2 = new Order();
    private static final OrderItem VALID_ORDER_ITEM_1 = new OrderItem();
    private static final OrderItem VALID_ORDER_ITEM_2 = new OrderItem();

    @Autowired
    private OrderRepository orderRepository;

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

        VALID_ORDER_ITEM_2.setId(2L);
        VALID_ORDER_ITEM_2.setBook(VALID_BOOK_2);
        VALID_ORDER_ITEM_2.setOrder(VALID_ORDER_2);
        VALID_ORDER_ITEM_2.setPrice(VALID_BOOK_2.getPrice());
        VALID_ORDER_ITEM_2.setQuantity(10);

        VALID_ORDER_2.setId(2L);
        VALID_ORDER_2.setStatus(Order.Status.PENDING);
        VALID_ORDER_2.setUser(USER);
        VALID_ORDER_2.setTotal(new BigDecimal(150));
        VALID_ORDER_2.setShippingAddress(USER.getShippingAddress());
        VALID_ORDER_2.setOrderDate(
                LocalDateTime.of(2023, 10, 18, 23, 39, 39));
        VALID_ORDER_2.setOrderItems(Set.of(VALID_ORDER_ITEM_2));
    }

    @Test
    @DisplayName("""
            Test the 'findAllOrders' method to retrieve all orders
            """)
    @Sql(scripts = SQL_SCRIPT_BEFORE_TEST_METHOD_EXECUTION_ADD_DATA,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = SQL_SCRIPT_AFTER_TEST_METHOD_EXECUTION_REMOVE_DATA,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllOrders_PageableArgument_ReturnListOfOrders() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Order> actual = orderRepository.findAllOrders(pageable);
        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertEquals(VALID_ORDER_1.getId(), actual.get(0).getId());
        assertEquals(VALID_ORDER_2.getId(), actual.get(1).getId());
    }
}
