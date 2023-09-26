package com.varukha.onlinebookstore.repository.book;

import static org.junit.jupiter.api.Assertions.*;

import com.varukha.onlinebookstore.model.Book;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
class BookRepositoryTest {
    private static final String SQL_SCRIPT_BEFORE_TEST_METHOD_EXECUTION_ADD_DATA =
            "classpath:database/book/add-book-and-categories-to-their-tables.sql";
    private static final String SQL_SCRIPT_AFTER_TEST_METHOD_EXECUTION_REMOVE_DATA =
            "classpath:database/book/remove-book-from-book-table.sql";
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("""
            Test 'findAllByCategoryId' method to retrieve the books by category ID
            """)
    @Sql(scripts = SQL_SCRIPT_BEFORE_TEST_METHOD_EXECUTION_ADD_DATA,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = SQL_SCRIPT_AFTER_TEST_METHOD_EXECUTION_REMOVE_DATA,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findAllByCategoryId_ValidCategoryId_ReturnBookListByCategoryId() {
        Long categoryId = 2L;

        Book firstBook = new Book();
        firstBook.setId(1L);
        firstBook.setTitle("Sample Book Title 3");
        firstBook.setAuthor("Author 3");
        firstBook.setIsbn("978-1234567893");
        firstBook.setPrice(BigDecimal.valueOf(24.99));
        firstBook.setDescription("Description 3");
        firstBook.setCoverImage("https://example.com/book-cover3.jpg");

        Book secondBook = new Book();
        secondBook.setId(2L);
        secondBook.setTitle("Sample Book Title 4");
        secondBook.setAuthor("Author 4");
        secondBook.setIsbn("978-1234567894");
        secondBook.setPrice(BigDecimal.valueOf(15.99));
        secondBook.setDescription("Description 4");
        secondBook.setCoverImage("https://example.com/book-cover4.jpg");

        List<Book> actual = bookRepository.findAllByCategoryId(categoryId);

//        assertEquals(2, actual.size());
        assertTrue(actual.contains(firstBook) && actual.contains(secondBook));

    }

    @Test
    @DisplayName("""
            Test 'findAllWithCategory' method to retrieve all
            books and book category information
            """)
    @Sql(scripts = SQL_SCRIPT_BEFORE_TEST_METHOD_EXECUTION_ADD_DATA,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = SQL_SCRIPT_AFTER_TEST_METHOD_EXECUTION_REMOVE_DATA,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findAllWithCategory_() {
        Book firstBook = new Book();
        firstBook.setId(1L);
        firstBook.setTitle("Sample Book Title 3");
        firstBook.setAuthor("Author 3");
        firstBook.setIsbn("978-1234567893");
        firstBook.setPrice(BigDecimal.valueOf(24.99));
        firstBook.setDescription("Description 3");
        firstBook.setCoverImage("https://example.com/book-cover3.jpg");
        firstBook.setCategories(Set.of());

        Book secondBook = new Book();
        secondBook.setId(2L);
        secondBook.setTitle("Sample Book Title 4");
        secondBook.setAuthor("Author 4");
        secondBook.setIsbn("978-1234567894");
        secondBook.setPrice(BigDecimal.valueOf(15.99));
        secondBook.setDescription("Description 4");
        secondBook.setCoverImage("https://example.com/book-cover4.jpg");

        Pageable pageable = PageRequest.of(0, 10);

        List<Book> books = bookRepository.findAllWithCategory(pageable);
        assertNotNull(books);
//        assertEquals(2, books.size());
        assertTrue(books.contains(firstBook) && books.contains(secondBook));
        assertEquals(books.get(0).getCategories(), firstBook.getCategories());
        assertEquals(books.get(1).getCategories(), secondBook.getCategories());
    }

    @Test
    void findByIdWithCategory() {
    }
}