package com.varukha.onlinebookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import com.varukha.onlinebookstore.model.Category;
import com.varukha.onlinebookstore.repository.book.BookRepository;
import com.varukha.onlinebookstore.repository.book.BookSpecificationBuilder;
import com.varukha.onlinebookstore.service.book.BookServiceImpl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
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
    private static final Category VALID_CATEGORY_1
            = new Category();
    private static final Category VALID_CATEGORY_2
            = new Category();
    private static final Book VALID_BOOK_1 = new Book();
    private static final Book VALID_BOOK_2 = new Book();
    private static final Book VALID_BOOK_3 = new Book();
    private static final Book VALID_BOOK_1_UPDATED = new Book();
    private static final CreateBookRequestDto REQUEST_DTO_BOOK_1
            = new CreateBookRequestDto();
    private static final CreateBookRequestDto REQUEST_DTO_BOOK_2
            = new CreateBookRequestDto();
    private static final CreateBookRequestDto REQUEST_DTO_BOOK_3
            = new CreateBookRequestDto();

    private static final BookDto BOOK_1_DTO = new BookDto();
    private static final BookDto BOOK_2_DTO = new BookDto();
    private static final BookDto BOOK_3_DTO = new BookDto();
    private static final BookDto BOOK_1_DTO_UPDATED = new BookDto();
    private static final BookDtoWithoutCategoryId BOOK_1_DTO_WITHOUT_CATEGORY_ID
            = new BookDtoWithoutCategoryId();
    private static final BookDtoWithoutCategoryId BOOK_2_DTO_WITHOUT_CATEGORY_ID
            = new BookDtoWithoutCategoryId();
    private static final BookDtoWithoutCategoryId BOOK_3_DTO_WITHOUT_CATEGORY_ID
            = new BookDtoWithoutCategoryId();

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

    @BeforeEach
    void setUp() {
        VALID_CATEGORY_1.setId(1L);
        VALID_CATEGORY_1.setName("Science Fiction");
        VALID_CATEGORY_1.setDescription("Science Fiction category description");

        VALID_CATEGORY_2.setId(2L);
        VALID_CATEGORY_2.setName("Mystery");
        VALID_CATEGORY_2.setDescription("Mystery category description");

        REQUEST_DTO_BOOK_1.setTitle("Sample Book Title 1");
        REQUEST_DTO_BOOK_1.setAuthor("Author 1");
        REQUEST_DTO_BOOK_1.setIsbn("978-1234567891");
        REQUEST_DTO_BOOK_1.setPrice(BigDecimal.valueOf(25));
        REQUEST_DTO_BOOK_1.setDescription("Description 1");
        REQUEST_DTO_BOOK_1.setCoverImage("https://example.com/book-cover1.jpg");
        REQUEST_DTO_BOOK_1.setCategoriesId(Set.of(VALID_CATEGORY_1.getId()));

        VALID_BOOK_1.setId(1L);
        VALID_BOOK_1.setTitle(REQUEST_DTO_BOOK_1.getTitle());
        VALID_BOOK_1.setAuthor(REQUEST_DTO_BOOK_1.getAuthor());
        VALID_BOOK_1.setIsbn(REQUEST_DTO_BOOK_1.getIsbn());
        VALID_BOOK_1.setPrice(REQUEST_DTO_BOOK_1.getPrice());
        VALID_BOOK_1.setDescription(REQUEST_DTO_BOOK_1.getDescription());
        VALID_BOOK_1.setCoverImage(REQUEST_DTO_BOOK_1.getCoverImage());
        VALID_BOOK_1.setCategories(Set.of(VALID_CATEGORY_1));

        BOOK_1_DTO.setId(1L);
        BOOK_1_DTO.setTitle(REQUEST_DTO_BOOK_1.getTitle());
        BOOK_1_DTO.setAuthor(REQUEST_DTO_BOOK_1.getAuthor());
        BOOK_1_DTO.setIsbn(REQUEST_DTO_BOOK_1.getIsbn());
        BOOK_1_DTO.setPrice(REQUEST_DTO_BOOK_1.getPrice());
        BOOK_1_DTO.setDescription(REQUEST_DTO_BOOK_1.getDescription());
        BOOK_1_DTO.setCoverImage(REQUEST_DTO_BOOK_1.getCoverImage());
        BOOK_1_DTO.setCategoriesId(Set.of(VALID_CATEGORY_1.getId()));

        BOOK_1_DTO_WITHOUT_CATEGORY_ID.setTitle("Sample Book Title 1");
        BOOK_1_DTO_WITHOUT_CATEGORY_ID.setTitle(REQUEST_DTO_BOOK_1.getTitle());
        BOOK_1_DTO_WITHOUT_CATEGORY_ID.setAuthor(REQUEST_DTO_BOOK_1.getAuthor());
        BOOK_1_DTO_WITHOUT_CATEGORY_ID.setIsbn(REQUEST_DTO_BOOK_1.getIsbn());
        BOOK_1_DTO_WITHOUT_CATEGORY_ID.setPrice(REQUEST_DTO_BOOK_1.getPrice());
        BOOK_1_DTO_WITHOUT_CATEGORY_ID.setDescription(REQUEST_DTO_BOOK_1.getDescription());
        BOOK_1_DTO_WITHOUT_CATEGORY_ID.setCoverImage(REQUEST_DTO_BOOK_1.getCoverImage());

        VALID_BOOK_1_UPDATED.setTitle("The Great Gatsby");
        VALID_BOOK_1_UPDATED.setAuthor("F. Scott Fitzgerald");
        VALID_BOOK_1_UPDATED.setIsbn("978-0743273565");
        VALID_BOOK_1_UPDATED.setPrice(BigDecimal.valueOf(150));
        VALID_BOOK_1_UPDATED.setDescription("The Great Gatsby is a classic novel");
        VALID_BOOK_1_UPDATED.setCoverImage("https://example.com/great-gatsby-cover.jpg");
        VALID_BOOK_1_UPDATED.setCategories(Set.of(VALID_CATEGORY_2));

        BOOK_1_DTO_UPDATED.setTitle(VALID_BOOK_1_UPDATED.getTitle());
        BOOK_1_DTO_UPDATED.setAuthor(VALID_BOOK_1_UPDATED.getAuthor());
        BOOK_1_DTO_UPDATED.setIsbn(VALID_BOOK_1_UPDATED.getIsbn());
        BOOK_1_DTO_UPDATED.setPrice(VALID_BOOK_1_UPDATED.getPrice());
        BOOK_1_DTO_UPDATED.setDescription(VALID_BOOK_1_UPDATED.getDescription());
        BOOK_1_DTO_UPDATED.setCoverImage(VALID_BOOK_1_UPDATED.getCoverImage());
        BOOK_1_DTO_UPDATED.setCategoriesId(Set.of(2L));

        REQUEST_DTO_BOOK_2.setTitle("Sample Book Title 2");
        REQUEST_DTO_BOOK_2.setAuthor("Author 2");
        REQUEST_DTO_BOOK_2.setIsbn("978-1234567892");
        REQUEST_DTO_BOOK_2.setPrice(BigDecimal.valueOf(16));
        REQUEST_DTO_BOOK_2.setDescription("Description 2");
        REQUEST_DTO_BOOK_2.setCoverImage("https://example.com/book-cover2.jpg");
        REQUEST_DTO_BOOK_2.setCategoriesId(Set.of(VALID_CATEGORY_2.getId()));

        VALID_BOOK_2.setId(2L);
        VALID_BOOK_2.setTitle(REQUEST_DTO_BOOK_2.getTitle());
        VALID_BOOK_2.setAuthor(REQUEST_DTO_BOOK_2.getAuthor());
        VALID_BOOK_2.setIsbn(REQUEST_DTO_BOOK_2.getIsbn());
        VALID_BOOK_2.setPrice(REQUEST_DTO_BOOK_2.getPrice());
        VALID_BOOK_2.setDescription(REQUEST_DTO_BOOK_2.getDescription());
        VALID_BOOK_2.setCoverImage(REQUEST_DTO_BOOK_2.getCoverImage());
        VALID_BOOK_2.setCategories(Set.of(VALID_CATEGORY_2));

        BOOK_2_DTO_WITHOUT_CATEGORY_ID.setTitle(REQUEST_DTO_BOOK_2.getTitle());
        BOOK_2_DTO_WITHOUT_CATEGORY_ID.setAuthor(REQUEST_DTO_BOOK_2.getAuthor());
        BOOK_2_DTO_WITHOUT_CATEGORY_ID.setIsbn(REQUEST_DTO_BOOK_2.getIsbn());
        BOOK_2_DTO_WITHOUT_CATEGORY_ID.setPrice(REQUEST_DTO_BOOK_2.getPrice());
        BOOK_2_DTO_WITHOUT_CATEGORY_ID.setDescription(REQUEST_DTO_BOOK_2.getDescription());
        BOOK_2_DTO_WITHOUT_CATEGORY_ID.setCoverImage(REQUEST_DTO_BOOK_2.getCoverImage());

        REQUEST_DTO_BOOK_3.setTitle("Sample Book Title 3");
        REQUEST_DTO_BOOK_3.setAuthor("Author 3");
        REQUEST_DTO_BOOK_3.setIsbn("978-1334567893");
        REQUEST_DTO_BOOK_3.setPrice(BigDecimal.valueOf(160));
        REQUEST_DTO_BOOK_3.setDescription("Description 3");
        REQUEST_DTO_BOOK_3.setCoverImage("https://example.com/book-cover3.jpg");
        REQUEST_DTO_BOOK_3.setCategoriesId(Set.of(VALID_CATEGORY_2.getId()));

        VALID_BOOK_3.setId(3L);
        VALID_BOOK_3.setTitle(REQUEST_DTO_BOOK_3.getTitle());
        VALID_BOOK_3.setAuthor(REQUEST_DTO_BOOK_3.getAuthor());
        VALID_BOOK_3.setIsbn(REQUEST_DTO_BOOK_3.getIsbn());
        VALID_BOOK_3.setPrice(REQUEST_DTO_BOOK_3.getPrice());
        VALID_BOOK_3.setDescription(REQUEST_DTO_BOOK_3.getDescription());
        VALID_BOOK_3.setCoverImage(REQUEST_DTO_BOOK_3.getCoverImage());
        VALID_BOOK_3.setCategories(Set.of(VALID_CATEGORY_2));

        BOOK_3_DTO_WITHOUT_CATEGORY_ID.setTitle(REQUEST_DTO_BOOK_3.getTitle());
        BOOK_3_DTO_WITHOUT_CATEGORY_ID.setAuthor(REQUEST_DTO_BOOK_3.getAuthor());
        BOOK_3_DTO_WITHOUT_CATEGORY_ID.setIsbn(REQUEST_DTO_BOOK_3.getIsbn());
        BOOK_3_DTO_WITHOUT_CATEGORY_ID.setPrice(REQUEST_DTO_BOOK_3.getPrice());
        BOOK_3_DTO_WITHOUT_CATEGORY_ID.setDescription(REQUEST_DTO_BOOK_3.getDescription());
        BOOK_3_DTO_WITHOUT_CATEGORY_ID.setCoverImage(REQUEST_DTO_BOOK_3.getCoverImage());
    }

    @Test
    @DisplayName("Test the 'save' method to add new book with valid request parameters")
    void save_WithValidCreateBookRequestDto_ReturnBookDto() {
        when(bookMapper.toModel(REQUEST_DTO_BOOK_1)).thenReturn(VALID_BOOK_1);
        when(bookRepository.save(VALID_BOOK_1)).thenReturn(VALID_BOOK_1);
        when(bookMapper.toDto(VALID_BOOK_1)).thenReturn(BOOK_1_DTO);

        BookDto actualBookDto = bookService.save(REQUEST_DTO_BOOK_1);

        assertNotNull(actualBookDto);
        assertThat(actualBookDto).isEqualTo(BOOK_1_DTO);
        assertEquals(BOOK_1_DTO, actualBookDto);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("""
            Test the 'getAll' method with valid request parameters
            and pagination to retrieve all books.
            """)
    void getAll_ValidBookDataRange_ReturnAllBookDtoList() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> bookList = getBookList();

        when(bookRepository.findAllWithCategory(pageable)).thenReturn(bookList);
        when(bookMapper.toDto(VALID_BOOK_1)).thenReturn(BOOK_1_DTO);
        when(bookMapper.toDto(VALID_BOOK_2)).thenReturn(BOOK_2_DTO);
        when(bookMapper.toDto(VALID_BOOK_3)).thenReturn(BOOK_3_DTO);

        List<BookDto> actualBookDtoList = bookService.getAll(pageable);

        assertNotNull(actualBookDtoList);
        assertThat(actualBookDtoList).hasSize(3);
        assertEquals(BOOK_1_DTO, actualBookDtoList.get(0));
        assertEquals(BOOK_2_DTO, actualBookDtoList.get(1));
        assertEquals(BOOK_3_DTO, actualBookDtoList.get(2));
        assertTrue(actualBookDtoList.contains(BOOK_1_DTO)
                && actualBookDtoList.contains(BOOK_2_DTO)
                && actualBookDtoList.contains(BOOK_3_DTO));
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("""
            Test the 'getById' method with valid request parameters
            to retrieve a book by its ID
            """)
    void getById_ValidBookId_ReturnValidBookData() {
        Long bookId = VALID_BOOK_1.getId();
        when(bookRepository.findByIdWithCategory(bookId))
                .thenReturn(Optional.of(VALID_BOOK_1));
        when(bookMapper.toDto(VALID_BOOK_1)).thenReturn(BOOK_1_DTO);

        BookDto result = bookService.getById(bookId);

        assertNotNull(result);
        assertEquals(BOOK_1_DTO, result);
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
        when(bookMapper.toModel(REQUEST_DTO_BOOK_1)).thenReturn(VALID_BOOK_1_UPDATED);
        when(bookRepository.save(VALID_BOOK_1_UPDATED)).thenReturn(VALID_BOOK_1_UPDATED);
        when(bookMapper.toDto(VALID_BOOK_1_UPDATED)).thenReturn(BOOK_1_DTO_UPDATED);

        Long bookId = VALID_BOOK_1.getId();
        BookDto result = bookService.update(bookId, REQUEST_DTO_BOOK_1);

        assertNotNull(result);
        assertEquals(BOOK_1_DTO_UPDATED, result);
    }

    @Test
    @DisplayName("""
            Test the 'search' method with valid input parameters.
            The method should return all books matching the specified input parameters
            """)
    void search_ValidParameters() {

        BookSearchParametersDto bookSearchParametersDto = new BookSearchParametersDto(
                new String[]{VALID_BOOK_1.getTitle()},
                new String[]{VALID_BOOK_1.getDescription()},
                new String[]{"1"}
        );
        when(bookSpecificationBuilder.build(bookSearchParametersDto))
                .thenReturn(bookSpecification);
        when(bookRepository.findAll(
                eq(bookSpecification),
                any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(VALID_BOOK_1)));
        when(bookMapper.toDto(VALID_BOOK_1)).thenReturn(BOOK_1_DTO);
        Pageable pageable = PageRequest.of(0, 10);
        List<BookDto> result = bookService.search(pageable, bookSearchParametersDto);
        List<BookDto> expectedBookDto = Collections.singletonList(BOOK_1_DTO);

        assertNotNull(result);
        assertEquals(expectedBookDto, result);
    }

    @Test
    @DisplayName("""
            Test the 'getByCategoryId' method with a valid category.
            The method should return all books matching the specified book category ID
            """)
    void getByCategoryId_ValidCategoryId_ReturnBookDtoWithoutCategoryId() {

        Long categoryId = VALID_CATEGORY_2.getId();
        List<Book> books = getBookList();
        when(bookRepository.findAllByCategoryId(categoryId)).thenReturn(books);
        when(bookMapper.toDtoWithoutCategories(VALID_BOOK_1))
                .thenReturn(BOOK_1_DTO_WITHOUT_CATEGORY_ID);
        when(bookMapper.toDtoWithoutCategories(VALID_BOOK_2))
                .thenReturn(BOOK_2_DTO_WITHOUT_CATEGORY_ID);
        when(bookMapper.toDtoWithoutCategories(VALID_BOOK_3))
                .thenReturn(BOOK_3_DTO_WITHOUT_CATEGORY_ID);

        List<BookDtoWithoutCategoryId> actualBookDtoList = bookService.getByCategoryId(categoryId);

        assertThat(actualBookDtoList).hasSize(3);
        assertTrue(actualBookDtoList.contains(BOOK_1_DTO_WITHOUT_CATEGORY_ID)
                && actualBookDtoList.contains(BOOK_2_DTO_WITHOUT_CATEGORY_ID)
                && actualBookDtoList.contains(BOOK_3_DTO_WITHOUT_CATEGORY_ID));
    }

    @NotNull
    private static List<Book> getBookList() {
        List<Book> bookList = new ArrayList<>();
        bookList.add(VALID_BOOK_1);
        bookList.add(VALID_BOOK_2);
        bookList.add(VALID_BOOK_3);
        return bookList;
    }
}
