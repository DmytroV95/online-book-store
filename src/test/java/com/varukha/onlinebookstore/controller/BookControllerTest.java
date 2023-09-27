package com.varukha.onlinebookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.varukha.onlinebookstore.dto.book.request.BookSearchParametersDto;
import com.varukha.onlinebookstore.dto.book.request.CreateBookRequestDto;
import com.varukha.onlinebookstore.dto.book.response.BookDto;
import com.varukha.onlinebookstore.dto.book.response.BookDtoWithoutCategoryId;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    protected static MockMvc mockMvc;
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
                    new ClassPathResource("database/book/add-book-and-categories-to-their-tables.sql")
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
                    new ClassPathResource("database/book/remove-books-and-categories-from-tables.sql")
            );
        }
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("""
            Test the 'getAll' endpoint with valid request parameters
            and pagination to retrieve all books.
            """)
    void getAll_ValidBookData_ReturnAllBookDtoList() throws Exception {
        CreateBookRequestDto bookRequestDto_1 = new CreateBookRequestDto();
        bookRequestDto_1.setTitle("Sample Book Title 3");
        bookRequestDto_1.setAuthor("Author 3");
        bookRequestDto_1.setIsbn("978-1234567893");
        bookRequestDto_1.setPrice(BigDecimal.valueOf(25));
        bookRequestDto_1.setDescription("Description 3");
        bookRequestDto_1.setCoverImage("https://example.com/book-cover3.jpg");
        bookRequestDto_1.setCategoriesId(Set.of(1L));

        BookDto bookResponseDto_1 = new BookDto();
        bookResponseDto_1.setId(1L);
        bookResponseDto_1.setTitle(bookRequestDto_1.getTitle());
        bookResponseDto_1.setAuthor(bookRequestDto_1.getAuthor());
        bookResponseDto_1.setIsbn(bookRequestDto_1.getIsbn());
        bookResponseDto_1.setPrice(bookRequestDto_1.getPrice());
        bookResponseDto_1.setDescription(bookRequestDto_1.getDescription());
        bookResponseDto_1.setCoverImage(bookRequestDto_1.getCoverImage());
        bookResponseDto_1.setCategoriesId(bookRequestDto_1.getCategoriesId());

        CreateBookRequestDto bookRequestDto_2 = new CreateBookRequestDto();
        bookRequestDto_2.setTitle("Sample Book Title 4");
        bookRequestDto_2.setAuthor("Author 4");
        bookRequestDto_2.setIsbn("978-1234567894");
        bookRequestDto_2.setPrice(BigDecimal.valueOf(16));
        bookRequestDto_2.setDescription("Description 4");
        bookRequestDto_2.setCoverImage("https://example.com/book-cover4.jpg");
        bookRequestDto_2.setCategoriesId(Set.of(2L));

        BookDto bookResponseDto_2 = new BookDto();
        bookResponseDto_2.setId(2L);
        bookResponseDto_2.setTitle(bookRequestDto_2.getTitle());
        bookResponseDto_2.setAuthor(bookRequestDto_2.getAuthor());
        bookResponseDto_2.setIsbn(bookRequestDto_2.getIsbn());
        bookResponseDto_2.setPrice(bookRequestDto_2.getPrice());
        bookResponseDto_2.setDescription(bookRequestDto_2.getDescription());
        bookResponseDto_2.setCoverImage(bookRequestDto_2.getCoverImage());
        bookResponseDto_2.setCategoriesId(bookRequestDto_2.getCategoriesId());

        CreateBookRequestDto bookRequestDto_3 = new CreateBookRequestDto();
        bookRequestDto_3.setTitle("Sample Book Title 2");
        bookRequestDto_3.setAuthor("Author 2");
        bookRequestDto_3.setIsbn("978-1334567892");
        bookRequestDto_3.setPrice(BigDecimal.valueOf(160));
        bookRequestDto_3.setDescription("Description 2");
        bookRequestDto_3.setCoverImage("https://example.com/book-cover2.jpg");
        bookRequestDto_3.setCategoriesId(Set.of(2L));

        BookDto bookResponseDto_3 = new BookDto();
        bookResponseDto_3.setId(3L);
        bookResponseDto_3.setTitle(bookRequestDto_3.getTitle());
        bookResponseDto_3.setAuthor(bookRequestDto_3.getAuthor());
        bookResponseDto_3.setIsbn(bookRequestDto_3.getIsbn());
        bookResponseDto_3.setPrice(bookRequestDto_3.getPrice());
        bookResponseDto_3.setDescription(bookRequestDto_3.getDescription());
        bookResponseDto_3.setCoverImage(bookRequestDto_3.getCoverImage());
        bookResponseDto_3.setCategoriesId(bookRequestDto_3.getCategoriesId());

        List<BookDto> expectedBookDto = new ArrayList<>();
        expectedBookDto.add(bookResponseDto_1);
        expectedBookDto.add(bookResponseDto_2);
        expectedBookDto.add(bookResponseDto_3);

        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto[] actualBookDto = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), BookDto[].class);
        assertNotNull(actualBookDto);
        assertEquals(expectedBookDto, Arrays.stream(actualBookDto).toList());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("""
            Test the 'getById' endpoint with valid request parameters
            to retrieve a book by its ID
            """)
    void getById_ValidBookId_ShouldReturnValidBookData() throws Exception {
        CreateBookRequestDto bookRequestDto_3 = new CreateBookRequestDto();
        bookRequestDto_3.setTitle("Sample Book Title 2");
        bookRequestDto_3.setAuthor("Author 2");
        bookRequestDto_3.setIsbn("978-1334567892");
        bookRequestDto_3.setPrice(BigDecimal.valueOf(160));
        bookRequestDto_3.setDescription("Description 2");
        bookRequestDto_3.setCoverImage("https://example.com/book-cover2.jpg");
        bookRequestDto_3.setCategoriesId(Set.of(2L));

        BookDto bookResponseDto_3 = new BookDto();
        bookResponseDto_3.setId(3L);
        bookResponseDto_3.setTitle(bookRequestDto_3.getTitle());
        bookResponseDto_3.setAuthor(bookRequestDto_3.getAuthor());
        bookResponseDto_3.setIsbn(bookRequestDto_3.getIsbn());
        bookResponseDto_3.setPrice(bookRequestDto_3.getPrice());
        bookResponseDto_3.setDescription(bookRequestDto_3.getDescription());
        bookResponseDto_3.setCoverImage(bookRequestDto_3.getCoverImage());
        bookResponseDto_3.setCategoriesId(bookRequestDto_3.getCategoriesId());

        Long bookId = bookResponseDto_3.getId();
        MvcResult result = mockMvc.perform(get("/books/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto actualBookDto = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        assertNotNull(actualBookDto);
        assertNotNull(actualBookDto.getId());
        assertEquals(bookResponseDto_3.getId(), actualBookDto.getId());
        EqualsBuilder.reflectionEquals(bookResponseDto_3, actualBookDto);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("""
            Test the 'save' endpoint to add new book with
            valid request parameters
            """)
    void save_WithValidCreateBookRequestDto_ReturnBookDto() throws Exception {
        CreateBookRequestDto bookRequestDto_1 = new CreateBookRequestDto();
        bookRequestDto_1.setTitle("Sample Book Title 1");
        bookRequestDto_1.setAuthor("Author 1");
        bookRequestDto_1.setIsbn("978-1234587693");
        bookRequestDto_1.setPrice(BigDecimal.valueOf(60));
        bookRequestDto_1.setDescription("Description 1");
        bookRequestDto_1.setCoverImage("https://example.com/book-cover1.jpg");
        bookRequestDto_1.setCategoriesId(Set.of(1L));

        BookDto bookResponseDto_1 = new BookDto();
        bookResponseDto_1.setId(1L);
        bookResponseDto_1.setTitle(bookRequestDto_1.getTitle());
        bookResponseDto_1.setAuthor(bookRequestDto_1.getAuthor());
        bookResponseDto_1.setIsbn(bookRequestDto_1.getIsbn());
        bookResponseDto_1.setPrice(bookRequestDto_1.getPrice());
        bookResponseDto_1.setDescription(bookRequestDto_1.getDescription());
        bookResponseDto_1.setCoverImage(bookRequestDto_1.getCoverImage());
        bookResponseDto_1.setCategoriesId(bookRequestDto_1.getCategoriesId());

        String jsonRequest = objectMapper.writeValueAsString(bookRequestDto_1);
        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        BookDto actualBookDto = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        assertNotNull(actualBookDto);
        assertNotNull(actualBookDto.getId());
        assertEquals(bookResponseDto_1.getTitle(), actualBookDto.getTitle());
        EqualsBuilder.reflectionEquals(bookResponseDto_1, actualBookDto);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Test the 'deleteById' endpoint with a valid book ID")
    void deleteById_ValidBookId_ReturnVoid() throws Exception {
        CreateBookRequestDto bookRequestDto_2 = new CreateBookRequestDto();
        bookRequestDto_2.setTitle("Sample Book Title 4");
        bookRequestDto_2.setAuthor("Author 4");
        bookRequestDto_2.setIsbn("978-1234567894");
        bookRequestDto_2.setPrice(BigDecimal.valueOf(16));
        bookRequestDto_2.setDescription("Description 4");
        bookRequestDto_2.setCoverImage("https://example.com/book-cover4.jpg");
        bookRequestDto_2.setCategoriesId(Set.of(2L));

        BookDto bookResponseDto_2 = new BookDto();
        bookResponseDto_2.setId(2L);
        bookResponseDto_2.setTitle(bookRequestDto_2.getTitle());
        bookResponseDto_2.setAuthor(bookRequestDto_2.getAuthor());
        bookResponseDto_2.setIsbn(bookRequestDto_2.getIsbn());
        bookResponseDto_2.setPrice(bookRequestDto_2.getPrice());
        bookResponseDto_2.setDescription(bookRequestDto_2.getDescription());
        bookResponseDto_2.setCoverImage(bookRequestDto_2.getCoverImage());
        bookResponseDto_2.setCategoriesId(bookRequestDto_2.getCategoriesId());

        Long bookId = bookResponseDto_2.getId();
        mockMvc.perform(delete("/books/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("""
            Test the 'update' endpoint with valid request parameters
            to update book data by ID
            """)
    void update_ValidCreateBookRequestDto_ReturnBookDto() throws Exception {
        CreateBookRequestDto bookRequestUpdateDto = new CreateBookRequestDto();
        bookRequestUpdateDto.setTitle("Sample Book Title 5");
        bookRequestUpdateDto.setAuthor("Author 5");
        bookRequestUpdateDto.setIsbn("978-1234567895");
        bookRequestUpdateDto.setPrice(BigDecimal.valueOf(50));
        bookRequestUpdateDto.setDescription("Description 5");
        bookRequestUpdateDto.setCoverImage("https://example.com/book-cover5.jpg");
        bookRequestUpdateDto.setCategoriesId(Set.of(2L));

        BookDto bookDtoUpdated = new BookDto();
        bookDtoUpdated.setId(1L);
        bookDtoUpdated.setTitle(bookRequestUpdateDto.getTitle());
        bookDtoUpdated.setAuthor(bookRequestUpdateDto.getAuthor());
        bookDtoUpdated.setIsbn(bookRequestUpdateDto.getIsbn());
        bookDtoUpdated.setPrice(bookRequestUpdateDto.getPrice());
        bookDtoUpdated.setDescription(bookRequestUpdateDto.getDescription());
        bookDtoUpdated.setCoverImage(bookRequestUpdateDto.getCoverImage());
        bookDtoUpdated.setCategoriesId(bookRequestUpdateDto.getCategoriesId());

        Long bookId = bookDtoUpdated.getId();
        String jsonRequest = objectMapper.writeValueAsString(bookRequestUpdateDto);
        MvcResult result = mockMvc.perform(put("/books/" + bookId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto actualBookDto = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        assertNotNull(actualBookDto);
        assertEquals(bookDtoUpdated.getTitle(), actualBookDto.getTitle());
        EqualsBuilder.reflectionEquals(bookDtoUpdated, actualBookDto);
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("""
            Test the 'search' endpoint with valid input parameters.
            The method should return all books matching the specified input parameters
            """)
    void search_ValidParameters() throws Exception {
        CreateBookRequestDto bookRequestDto_2 = new CreateBookRequestDto();
        bookRequestDto_2.setTitle("Sample Book Title 4");
        bookRequestDto_2.setAuthor("Author 4");
        bookRequestDto_2.setIsbn("978-1234567894");
        bookRequestDto_2.setPrice(BigDecimal.valueOf(16));
        bookRequestDto_2.setDescription("Description 4");
        bookRequestDto_2.setCoverImage("https://example.com/book-cover4.jpg");
        bookRequestDto_2.setCategoriesId(Set.of(2L));

        BookDto bookResponseDto_2 = new BookDto();
        bookResponseDto_2.setId(2L);
        bookResponseDto_2.setTitle(bookRequestDto_2.getTitle());
        bookResponseDto_2.setAuthor(bookRequestDto_2.getAuthor());
        bookResponseDto_2.setIsbn(bookRequestDto_2.getIsbn());
        bookResponseDto_2.setPrice(bookRequestDto_2.getPrice());
        bookResponseDto_2.setDescription(bookRequestDto_2.getDescription());
        bookResponseDto_2.setCoverImage(bookRequestDto_2.getCoverImage());
        bookResponseDto_2.setCategoriesId(bookRequestDto_2.getCategoriesId());

        List<BookDto> bookDtoList = new ArrayList<>();
        bookDtoList.add(bookResponseDto_2);
        BookSearchParametersDto params = new BookSearchParametersDto(
                new String[]{bookRequestDto_2.getTitle()},
                new String[]{bookRequestDto_2.getAuthor()},
                new String[]{String.valueOf(bookRequestDto_2.getCategoriesId())});
        MvcResult result = mockMvc.perform(get("/books/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("titles", params.titles())
                        .param("authors", params.authors())
                        .param("category", params.categories()))
                .andExpect(status().isOk())
                .andReturn();
        BookDto[] actualBookDto = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), BookDto[].class);
        assertNotNull(actualBookDto);
        assertEquals(bookDtoList, Arrays.stream(actualBookDto).toList());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("""
            Test the 'getByCategoryId' endpoint with a valid category.
            The method should return all books matching the specified book category ID
            """)
    void getByCategoryId_ValidCategoryId_ReturnBookDtoWithoutCategoryId() throws Exception {
        CreateBookRequestDto bookRequestDto_1 = new CreateBookRequestDto();
        bookRequestDto_1.setTitle("Sample Book Title 3");
        bookRequestDto_1.setAuthor("Author 3");
        bookRequestDto_1.setIsbn("978-1234567893");
        bookRequestDto_1.setPrice(BigDecimal.valueOf(25));
        bookRequestDto_1.setDescription("Description 3");
        bookRequestDto_1.setCoverImage("https://example.com/book-cover3.jpg");
        bookRequestDto_1.setCategoriesId(Set.of(1L));

        BookDtoWithoutCategoryId bookDtoWithoutCategoryId = new BookDtoWithoutCategoryId();
        bookDtoWithoutCategoryId.setId(1L);
        bookDtoWithoutCategoryId.setTitle(bookRequestDto_1.getTitle());
        bookDtoWithoutCategoryId.setAuthor(bookRequestDto_1.getAuthor());
        bookDtoWithoutCategoryId.setIsbn(bookRequestDto_1.getIsbn());
        bookDtoWithoutCategoryId.setPrice(bookRequestDto_1.getPrice());
        bookDtoWithoutCategoryId.setDescription(bookRequestDto_1.getDescription());
        bookDtoWithoutCategoryId.setCoverImage(bookRequestDto_1.getCoverImage());

        List<BookDtoWithoutCategoryId> expectedBookDto = new ArrayList<>();
        expectedBookDto.add(bookDtoWithoutCategoryId);

        MvcResult result = mockMvc.perform(get("/books/1/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDtoWithoutCategoryId[] actualBookDtoWithoutCategoryId = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), BookDtoWithoutCategoryId[].class);
        assertNotNull(actualBookDtoWithoutCategoryId);
        assertEquals(expectedBookDto, Arrays.stream(actualBookDtoWithoutCategoryId).toList());
    }
}
