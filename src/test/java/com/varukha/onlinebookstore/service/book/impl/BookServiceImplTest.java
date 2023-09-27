package com.varukha.onlinebookstore.service.book.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.varukha.onlinebookstore.dto.book.request.BookSearchParametersDto;
import com.varukha.onlinebookstore.dto.book.request.CreateBookRequestDto;
import com.varukha.onlinebookstore.dto.book.response.BookDto;
import com.varukha.onlinebookstore.dto.book.response.BookDtoWithoutCategoryId;
import com.varukha.onlinebookstore.exception.EntityNotFoundException;
import com.varukha.onlinebookstore.mapper.BookMapper;
import com.varukha.onlinebookstore.model.Book;
import com.varukha.onlinebookstore.repository.book.BookRepository;
import com.varukha.onlinebookstore.repository.book.BookSpecificationBuilder;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookServiceImpl bookService;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private Specification<Book> bookSpecification;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @Test
    @DisplayName("Test the 'save' method to add new book with valid request parameters")
    void save_WithValidCreateBookRequestDto_ReturnBookDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Sample Book Title");
        requestDto.setAuthor("John Doe");
        requestDto.setIsbn("978-1234567890");
        requestDto.setPrice(BigDecimal.valueOf(19.99));
        requestDto.setDescription("This is a sample book description.");
        requestDto.setCoverImage("https://example.com/book-cover.jpg");

        Book book = new Book();
        book.setTitle(requestDto.getTitle());
        book.setAuthor(requestDto.getAuthor());
        book.setIsbn(requestDto.getIsbn());
        book.setPrice(requestDto.getPrice());
        book.setDescription(requestDto.getDescription());
        book.setCoverImage(requestDto.getCoverImage());

        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());
        bookDto.setDescription(book.getDescription());
        bookDto.setCoverImage(book.getCoverImage());

        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.save(requestDto);

        assertNotNull(result);
        assertThat(result).isEqualTo(bookDto);
        assertEquals(bookDto, result);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("""
            Test the 'getAll' method with valid request parameters
            and pagination to retrieve all books.
            """)
    void getAll_ValidBookDataRange_ReturnAllBookDtoList() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Sample Book Title");
        book.setAuthor("John Doe");
        book.setIsbn("978-1234567890");
        book.setPrice(BigDecimal.valueOf(19.99));
        book.setDescription("This is a sample book description.");
        book.setCoverImage("https://example.com/book-cover.jpg");

        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());
        bookDto.setDescription(book.getDescription());
        bookDto.setCoverImage(book.getCoverImage());

        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = Collections.singletonList(book);

        when(bookRepository.findAllWithCategory(pageable)).thenReturn(books);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> bookDtoList = bookService.getAll(pageable);

        assertNotNull(bookDtoList);
        assertThat(bookDtoList).hasSize(1);
        assertThat(bookDtoList.get(0)).isEqualTo(bookDto);
        verify(bookRepository, times(1)).findAllWithCategory(pageable);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("""
            Test the 'getById' method with valid request parameters
            to retrieve a book by its ID
            """)
    void getById_ValidBookId_ShouldReturnValidBookData() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Sample Book Title");
        book.setAuthor("John Doe");
        book.setIsbn("978-1234567890");
        book.setPrice(BigDecimal.valueOf(19.99));
        book.setDescription("This is a sample book description.");
        book.setCoverImage("https://example.com/book-cover.jpg");

        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());
        bookDto.setDescription(book.getDescription());
        bookDto.setCoverImage(book.getCoverImage());

        Long bookId = 1L;

        when(bookRepository.findByIdWithCategory(bookId))
                .thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.getById(bookId);

        assertNotNull(result);
        assertEquals(bookDto, result);
        verify(bookRepository, times(1))
                .findByIdWithCategory(bookId);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("""
            Test the 'getById' method with a non-existent book ID.
            The method should throw an EntityNotFoundException.
            """)
    void getById_WithNonExistedBookId_ShouldThrowException() {
        Long bookId = 100L;
        when(bookRepository.findByIdWithCategory(bookId))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.getById(bookId));
        String expected = "Can't find book by id: " + bookId;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test the 'deleteById' method with a valid book ID")
    void deleteById_ValidBookId_ReturnVoid() {
        Long bookId = 1L;
        bookService.deleteById(bookId);
        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    @DisplayName("""
            Test the 'update' method with valid request parameters
            to update book data by ID
            """)
    void update_ValidCreateBookRequestDto_ReturnBookDto() {
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setTitle("Sample Book Title");
        bookRequestDto.setAuthor("John Doe");
        bookRequestDto.setIsbn("978-1234567890");
        bookRequestDto.setPrice(BigDecimal.valueOf(19.99));
        bookRequestDto.setDescription("This is a sample book description.");
        bookRequestDto.setCoverImage("https://example.com/book-cover.jpg");

        Book updatedBook = new Book();
        updatedBook.setId(1L);
        updatedBook.setTitle("The Great Gatsby");
        updatedBook.setAuthor("F. Scott Fitzgerald");
        updatedBook.setIsbn("978-0743273565");
        updatedBook.setPrice(BigDecimal.valueOf(50));
        updatedBook.setDescription("The Great Gatsby is a classic novel");
        updatedBook.setCoverImage("https://example.com/great-gatsby-cover.jpg");

        BookDto bookDto = new BookDto();
        bookDto.setId(updatedBook.getId());
        bookDto.setTitle(updatedBook.getTitle());
        bookDto.setAuthor(updatedBook.getAuthor());
        bookDto.setIsbn(updatedBook.getIsbn());
        bookDto.setPrice(updatedBook.getPrice());
        bookDto.setDescription(updatedBook.getDescription());
        bookDto.setCoverImage(updatedBook.getCoverImage());

        when(bookMapper.toModel(bookRequestDto)).thenReturn(updatedBook);
        when(bookRepository.save(updatedBook)).thenReturn(updatedBook);
        when(bookMapper.toDto(updatedBook)).thenReturn(bookDto);

        Long bookId = 1L;
        BookDto result = bookService.update(bookId, bookRequestDto);

        assertNotNull(result);
        assertEquals(bookDto, result);
    }

    @Test
    @DisplayName("""
            Test the 'search' method with valid input parameters.
            The method should return all books matching the specified input parameters
            """)
    void search_ValidParameters() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Sample Book Title");
        book.setAuthor("John Doe");
        book.setIsbn("978-1234567890");
        book.setPrice(BigDecimal.valueOf(19.99));
        book.setDescription("This is a sample book description.");
        book.setCoverImage("https://example.com/book-cover.jpg");

        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());
        bookDto.setDescription(book.getDescription());
        bookDto.setCoverImage(book.getCoverImage());

        BookSearchParametersDto bookSearchParametersDto = new BookSearchParametersDto(
                new String[]{"Sample Book Title", "The Great Gatsby"},
                new String[]{"John Doe", "F. Scott Fitzgerald"},
                new String[]{"1"}
        );

        when(bookSpecificationBuilder.build(bookSearchParametersDto))
                .thenReturn(bookSpecification);
        when(bookRepository.findAll(
                eq(bookSpecification),
                any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(book)));
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        Pageable pageable = PageRequest.of(0, 10);
        List<BookDto> result = bookService.search(pageable, bookSearchParametersDto);
        List<BookDto> expectedBookDto = Collections.singletonList(bookDto);

        assertNotNull(result);
        assertEquals(expectedBookDto, result);
    }

    @Test
    @DisplayName("""
            Test the 'getByCategoryId' method with a valid category.
            The method should return all books matching the specified book category ID
            """)
    void getByCategoryId_ValidCategoryId_ReturnBookDtoWithoutCategoryId() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Sample Book Title");
        book.setAuthor("John Doe");
        book.setIsbn("978-1234567890");
        book.setPrice(BigDecimal.valueOf(19.99));
        book.setDescription("This is a sample book description.");
        book.setCoverImage("https://example.com/book-cover.jpg");

        BookDtoWithoutCategoryId bookDtoWithoutCategoryId = new BookDtoWithoutCategoryId();
        bookDtoWithoutCategoryId.setId(book.getId());
        bookDtoWithoutCategoryId.setTitle(book.getTitle());
        bookDtoWithoutCategoryId.setAuthor(book.getAuthor());
        bookDtoWithoutCategoryId.setIsbn(book.getIsbn());
        bookDtoWithoutCategoryId.setPrice(book.getPrice());
        bookDtoWithoutCategoryId.setDescription(book.getDescription());
        bookDtoWithoutCategoryId.setCoverImage(book.getCoverImage());

        Long categoryId = 1L;
        List<Book> books = Collections.singletonList(book);
        List<BookDtoWithoutCategoryId> bookDtoList = Collections
                .singletonList(bookDtoWithoutCategoryId);
        when(bookRepository.findAllByCategoryId(categoryId)).thenReturn(books);
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(bookDtoList.get(0));

        List<BookDtoWithoutCategoryId> result = bookService.getByCategoryId(categoryId);

        assertThat(bookDtoList).hasSize(1);
        assertEquals(bookDtoList.get(0), result.get(0));
    }
}
