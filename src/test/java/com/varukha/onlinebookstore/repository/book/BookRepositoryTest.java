package com.varukha.onlinebookstore.repository.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.varukha.onlinebookstore.model.Book;
import com.varukha.onlinebookstore.model.Category;
import java.math.BigDecimal;
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
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    private static final String SQL_SCRIPT_BEFORE_TEST_METHOD_EXECUTION_ADD_DATA =
            "classpath:database/book/add-book-and-categories-to-their-tables.sql";
    private static final String SQL_SCRIPT_AFTER_TEST_METHOD_EXECUTION_REMOVE_DATA =
            "classpath:database/book/remove-books-and-categories-from-tables.sql";
    private static final Category VALID_CATEGORY_1 = new Category();
    private static final Category VALID_CATEGORY_2 = new Category();
    private static final Book VALID_BOOK_1 = new Book();
    private static final Book VALID_BOOK_2 = new Book();
    private static final Book VALID_BOOK_3 = new Book();

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        VALID_CATEGORY_1.setId(1L);
        VALID_CATEGORY_1.setName("Science Fiction");
        VALID_CATEGORY_1.setDescription("Science Fiction category description");

        VALID_CATEGORY_2.setId(2L);
        VALID_CATEGORY_2.setName("Mystery");
        VALID_CATEGORY_2.setDescription("Mystery category description");

        VALID_BOOK_1.setId(1L);
        VALID_BOOK_1.setTitle("Sample Book Title 1");
        VALID_BOOK_1.setAuthor("Author 1");
        VALID_BOOK_1.setIsbn("978-1234567891");
        VALID_BOOK_1.setPrice(BigDecimal.valueOf(25));
        VALID_BOOK_1.setDescription("Description 1");
        VALID_BOOK_1.setCoverImage("https://example.com/book-cover1.jpg");
        VALID_BOOK_1.setCategories(Set.of(VALID_CATEGORY_1));

        VALID_BOOK_2.setId(2L);
        VALID_BOOK_2.setTitle("Sample Book Title 2");
        VALID_BOOK_2.setAuthor("Author 2");
        VALID_BOOK_2.setIsbn("978-1234567892");
        VALID_BOOK_2.setPrice(BigDecimal.valueOf(16));
        VALID_BOOK_2.setDescription("Description 2");
        VALID_BOOK_2.setCoverImage("https://example.com/book-cover2.jpg");
        VALID_BOOK_3.setCategories(Set.of(VALID_CATEGORY_2));

        VALID_BOOK_3.setId(3L);
        VALID_BOOK_3.setTitle("Sample Book Title 3");
        VALID_BOOK_3.setAuthor("Author 3");
        VALID_BOOK_3.setIsbn("978-1334567893");
        VALID_BOOK_3.setPrice(BigDecimal.valueOf(160));
        VALID_BOOK_3.setDescription("Description 3");
        VALID_BOOK_3.setCoverImage("https://example.com/book-cover3.jpg");
        VALID_BOOK_3.setCategories(Set.of(VALID_CATEGORY_2));
    }

    @Test
    @DisplayName("""
            Test 'findAllByCategoryId' method to retrieve the books by category ID
            """)
    @Sql(scripts = SQL_SCRIPT_BEFORE_TEST_METHOD_EXECUTION_ADD_DATA,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = SQL_SCRIPT_AFTER_TEST_METHOD_EXECUTION_REMOVE_DATA,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategoryId_ValidCategoryId_ReturnBookListByCategoryId() {
        Long categoryId = 2L;
        List<Book> actual = bookRepository.findAllByCategoryId(categoryId);
        assertNotNull(actual);
        assertEquals(2, actual.size());
        EqualsBuilder.reflectionEquals(VALID_BOOK_2, actual.get(0));
        EqualsBuilder.reflectionEquals(VALID_BOOK_3, actual.get(1));
    }

    @Test
    @DisplayName("""
            Test 'findAllWithCategory' method to retrieve all
            books and book category information
            """)
    @Sql(scripts = SQL_SCRIPT_BEFORE_TEST_METHOD_EXECUTION_ADD_DATA,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = SQL_SCRIPT_AFTER_TEST_METHOD_EXECUTION_REMOVE_DATA,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllWithCategory_() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> actual = bookRepository.findAllWithCategory(pageable);
        assertNotNull(actual);
        assertEquals(3, actual.size());
        EqualsBuilder.reflectionEquals(VALID_BOOK_1, actual.get(0));
        EqualsBuilder.reflectionEquals(VALID_BOOK_2, actual.get(1));
        EqualsBuilder.reflectionEquals(VALID_BOOK_3, actual.get(2));
    }

    @Test
    @DisplayName("""
            Test 'findByIdWithCategory' method to retrieve book by ID
            with book category information
            """)
    @Sql(scripts = SQL_SCRIPT_BEFORE_TEST_METHOD_EXECUTION_ADD_DATA,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = SQL_SCRIPT_AFTER_TEST_METHOD_EXECUTION_REMOVE_DATA,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByIdWithCategory_ValidBookId_ReturnBookById() {
        Book actual = bookRepository.findByIdWithCategory(VALID_BOOK_1.getId())
                .orElse(null);
        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(VALID_BOOK_1, actual);
    }
}
