package com.varukha.onlinebookstore.service.book.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
import com.varukha.onlinebookstore.service.book.BookService;
import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @Mock private BookRepository bookRepository;
    @InjectMocks private BookServiceImpl bookService;
    @Mock private BookMapper bookMapper;

    @Test
    @DisplayName("Save new book with valid request parameters")
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
    void getById_ValidBookId_ShouldReturnValidBookData() {
        Long bookId = 1L;
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

        when(bookRepository.findByIdWithCategory(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.getById(bookId);

        assertNotNull(result);
        assertEquals(bookDto, result);
        verify(bookRepository, times(1)).findByIdWithCategory(bookId);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
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
    void getById_WithNegativeBookId_ShouldThrowException() {
        Long bookId = -1L;
        when(bookRepository.findByIdWithCategory(bookId))
                .thenThrow(new EntityNotFoundException("Can't find "
                        + "book by id: " + bookId));
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.getById(bookId));
        assertEquals("Can't find "
                + "book by id: " + bookId, exception.getMessage());
    }

    @Test
    void deleteById_ValidBookId_ReturnVoid() {
        Long bookId = 1L;
        bookService.deleteById(bookId);
        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    void update_ValidCreateBookRequestDto_ReturnBookDto() {
        Long bookId = 1L;
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

        BookDto result = bookService.update(bookId, bookRequestDto);

        assertNotNull(result);
        assertEquals(bookDto, result);
    }

    @Test
    void search() {
//        Pageable pageable = Pageable.unpaged();
//        BookSearchParametersDto params = new BookSearchParametersDto();
//        List<Book> books = List.of(new Book());
//        List<BookDto> bookDtos = List.of(new BookDto());
//
//        when(bookSpecificationBuilder.build(params)).thenReturn(/* Your Specification */);
//        when(bookRepository.findAll(any(), eq(pageable))).thenReturn(books);
//        when(bookMapper.toDto(any(Book.class))).thenReturn(bookDtos.get(0));
//
//        List<BookDto> result = bookService.search(pageable, params);
//
//        assertNotNull(result);
//        assertEquals(bookDtos.size(), result.size());
    }

    @Test
    void getByCategoryId() {
        Long categoryId = 1L;
        List<Book> books = List.of(new Book());
        List<BookDtoWithoutCategoryId> bookDtoList = List.of(new BookDtoWithoutCategoryId());

        when(bookRepository.findAllByCategoryId(categoryId)).thenReturn(books);
        when(bookMapper.toDtoWithoutCategories(any(Book.class))).thenReturn(bookDtoList.get(0));

        List<BookDtoWithoutCategoryId> result = bookService.getByCategoryId(categoryId);

        assertNotNull(result);
        assertEquals(bookDtoList.size(), result.size());
    }
}