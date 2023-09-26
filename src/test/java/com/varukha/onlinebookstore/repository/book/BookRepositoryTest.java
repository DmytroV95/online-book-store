package com.varukha.onlinebookstore.repository.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.varukha.onlinebookstore.model.Book;
import com.varukha.onlinebookstore.model.Category;
import java.math.BigDecimal;
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
            "classpath:database/book/remove-books-and-categories-from-tables.sql";
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("""
            Test 'findAllByCategoryId' method to retrieve the books by category ID
            """)
    @Sql(scripts = SQL_SCRIPT_BEFORE_TEST_METHOD_EXECUTION_ADD_DATA,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = SQL_SCRIPT_AFTER_TEST_METHOD_EXECUTION_REMOVE_DATA,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategoryId_ValidCategoryId_ReturnBookListByCategoryId() {
        Category scienceFiction = new Category();
        scienceFiction.setId(1L);
        scienceFiction.setName("Science Fiction");
        scienceFiction.setDescription("Science Fiction category description");
        scienceFiction.setDeleted(false);

        Category mystery = new Category();
        mystery.setId(2L);
        mystery.setName("Mystery");
        mystery.setDescription("Mystery category description");
        mystery.setDeleted(false);

        Long categoryId = 2L;

        Book firstBook = new Book();
        firstBook.setId(1L);
        firstBook.setTitle("Sample Book Title 3");
        firstBook.setAuthor("Author 3");
        firstBook.setIsbn("978-1234567893");
        firstBook.setPrice(BigDecimal.valueOf(25));
        firstBook.setDescription("Description 3");
        firstBook.setCoverImage("https://example.com/book-cover3.jpg");
        firstBook.setCategories(Set.of(scienceFiction));
        firstBook.setDeleted(false);

        Book secondBook = new Book();
        secondBook.setId(2L);
        secondBook.setTitle("Sample Book Title 4");
        secondBook.setAuthor("Author 4");
        secondBook.setIsbn("978-1234567894");
        secondBook.setPrice(BigDecimal.valueOf(16));
        secondBook.setDescription("Description 4");
        secondBook.setCoverImage("https://example.com/book-cover4.jpg");
        secondBook.setCategories(Set.of(mystery));
        secondBook.setDeleted(false);

        List<Book> actual = bookRepository.findAllByCategoryId(categoryId);
        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(secondBook, actual.get(0));
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
        Category scienceFiction = new Category();
        scienceFiction.setId(1L);
        scienceFiction.setName("Science Fiction");
        scienceFiction.setDescription("Science Fiction category description");
        scienceFiction.setDeleted(false);

        Category mystery = new Category();
        mystery.setId(2L);
        mystery.setName("Mystery");
        mystery.setDescription("Mystery category description");
        mystery.setDeleted(false);

        Book firstBook = new Book();
        firstBook.setId(1L);
        firstBook.setTitle("Sample Book Title 3");
        firstBook.setAuthor("Author 3");
        firstBook.setIsbn("978-1234567893");
        firstBook.setPrice(BigDecimal.valueOf(25));
        firstBook.setDescription("Description 3");
        firstBook.setCoverImage("https://example.com/book-cover3.jpg");
        firstBook.setCategories(Set.of(scienceFiction));
        firstBook.setDeleted(false);

        Book secondBook = new Book();
        secondBook.setId(2L);
        secondBook.setTitle("Sample Book Title 4");
        secondBook.setAuthor("Author 4");
        secondBook.setIsbn("978-1234567894");
        secondBook.setPrice(BigDecimal.valueOf(16));
        secondBook.setDescription("Description 4");
        secondBook.setCoverImage("https://example.com/book-cover4.jpg");
        secondBook.setCategories(Set.of(mystery));
        secondBook.setDeleted(false);

        Pageable pageable = PageRequest.of(0, 10);

        List<Book> books = bookRepository.findAllWithCategory(pageable);
        assertNotNull(books);
        assertEquals(2, books.size());
        assertTrue(books.contains(firstBook) && books.contains(secondBook));
        assertEquals(books.get(0).getCategories(), firstBook.getCategories());
        assertEquals(books.get(1).getCategories(), secondBook.getCategories());
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
        Category scienceFiction = new Category();
        scienceFiction.setId(1L);
        scienceFiction.setName("Science Fiction");
        scienceFiction.setDescription("Science Fiction category description");
        scienceFiction.setDeleted(false);

        Long bookId = 1L;

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Sample Book Title 3");
        book.setAuthor("Author 3");
        book.setIsbn("978-1234567893");
        book.setPrice(BigDecimal.valueOf(25));
        book.setDescription("Description 3");
        book.setCoverImage("https://example.com/book-cover3.jpg");
        book.setCategories(Set.of(scienceFiction));
        book.setDeleted(false);

        Book actual = bookRepository.findByIdWithCategory(bookId)
                .orElse(null);
        assertNotNull(actual);
        assertEquals(book, actual);
    }
}
