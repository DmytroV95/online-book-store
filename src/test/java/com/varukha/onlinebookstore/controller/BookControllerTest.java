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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
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
                    new ClassPathResource(
                            "database/book/add-book-and-categories-to-their-tables.sql")
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
                    new ClassPathResource(
                            "database/book/remove-books-and-categories-from-tables.sql")
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
        CreateBookRequestDto firstBookRequestDto = new CreateBookRequestDto();
        firstBookRequestDto.setTitle("Sample Book Title 3");
        firstBookRequestDto.setAuthor("Author 3");
        firstBookRequestDto.setIsbn("978-1234567893");
        firstBookRequestDto.setPrice(BigDecimal.valueOf(25));
        firstBookRequestDto.setDescription("Description 3");
        firstBookRequestDto.setCoverImage("https://example.com/book-cover3.jpg");
        firstBookRequestDto.setCategoriesId(Set.of(1L));

        BookDto firstBookResponseDto = new BookDto();
        firstBookResponseDto.setId(1L);
        firstBookResponseDto.setTitle(firstBookRequestDto.getTitle());
        firstBookResponseDto.setAuthor(firstBookRequestDto.getAuthor());
        firstBookResponseDto.setIsbn(firstBookRequestDto.getIsbn());
        firstBookResponseDto.setPrice(firstBookRequestDto.getPrice());
        firstBookResponseDto.setDescription(firstBookRequestDto.getDescription());
        firstBookResponseDto.setCoverImage(firstBookRequestDto.getCoverImage());
        firstBookResponseDto.setCategoriesId(firstBookRequestDto.getCategoriesId());

        CreateBookRequestDto secondBookRequestDto = new CreateBookRequestDto();
        secondBookRequestDto.setTitle("Sample Book Title 4");
        secondBookRequestDto.setAuthor("Author 4");
        secondBookRequestDto.setIsbn("978-1234567894");
        secondBookRequestDto.setPrice(BigDecimal.valueOf(16));
        secondBookRequestDto.setDescription("Description 4");
        secondBookRequestDto.setCoverImage("https://example.com/book-cover4.jpg");
        secondBookRequestDto.setCategoriesId(Set.of(2L));

        BookDto secondBookResponseDto = new BookDto();
        secondBookResponseDto.setId(2L);
        secondBookResponseDto.setTitle(secondBookRequestDto.getTitle());
        secondBookResponseDto.setAuthor(secondBookRequestDto.getAuthor());
        secondBookResponseDto.setIsbn(secondBookRequestDto.getIsbn());
        secondBookResponseDto.setPrice(secondBookRequestDto.getPrice());
        secondBookResponseDto.setDescription(secondBookRequestDto.getDescription());
        secondBookResponseDto.setCoverImage(secondBookRequestDto.getCoverImage());
        secondBookResponseDto.setCategoriesId(secondBookRequestDto.getCategoriesId());

        CreateBookRequestDto thirdBookRequestDto = new CreateBookRequestDto();
        thirdBookRequestDto.setTitle("Sample Book Title 2");
        thirdBookRequestDto.setAuthor("Author 2");
        thirdBookRequestDto.setIsbn("978-1334567892");
        thirdBookRequestDto.setPrice(BigDecimal.valueOf(160));
        thirdBookRequestDto.setDescription("Description 2");
        thirdBookRequestDto.setCoverImage("https://example.com/book-cover2.jpg");
        thirdBookRequestDto.setCategoriesId(Set.of(2L));

        BookDto thirdBookResponseDto = new BookDto();
        thirdBookResponseDto.setId(3L);
        thirdBookResponseDto.setTitle(thirdBookRequestDto.getTitle());
        thirdBookResponseDto.setAuthor(thirdBookRequestDto.getAuthor());
        thirdBookResponseDto.setIsbn(thirdBookRequestDto.getIsbn());
        thirdBookResponseDto.setPrice(thirdBookRequestDto.getPrice());
        thirdBookResponseDto.setDescription(thirdBookRequestDto.getDescription());
        thirdBookResponseDto.setCoverImage(thirdBookRequestDto.getCoverImage());
        thirdBookResponseDto.setCategoriesId(thirdBookRequestDto.getCategoriesId());

        List<BookDto> expectedBookDto = new ArrayList<>();
        expectedBookDto.add(firstBookResponseDto);
        expectedBookDto.add(secondBookResponseDto);
        expectedBookDto.add(thirdBookResponseDto);

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
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setTitle("Sample Book Title 2");
        bookRequestDto.setAuthor("Author 2");
        bookRequestDto.setIsbn("978-1334567892");
        bookRequestDto.setPrice(BigDecimal.valueOf(160));
        bookRequestDto.setDescription("Description 2");
        bookRequestDto.setCoverImage("https://example.com/book-cover2.jpg");
        bookRequestDto.setCategoriesId(Set.of(2L));

        BookDto bookResponseDto = new BookDto();
        bookResponseDto.setId(3L);
        bookResponseDto.setTitle(bookRequestDto.getTitle());
        bookResponseDto.setAuthor(bookRequestDto.getAuthor());
        bookResponseDto.setIsbn(bookRequestDto.getIsbn());
        bookResponseDto.setPrice(bookRequestDto.getPrice());
        bookResponseDto.setDescription(bookRequestDto.getDescription());
        bookResponseDto.setCoverImage(bookRequestDto.getCoverImage());
        bookResponseDto.setCategoriesId(bookRequestDto.getCategoriesId());

        Long bookId = bookResponseDto.getId();
        MvcResult result = mockMvc.perform(get("/books/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto actualBookDto = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        assertNotNull(actualBookDto);
        assertNotNull(actualBookDto.getId());
        assertEquals(bookResponseDto.getId(), actualBookDto.getId());
        EqualsBuilder.reflectionEquals(bookResponseDto, actualBookDto);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("""
            Test the 'save' endpoint to add new book with
            valid request parameters
            """)
    void save_WithValidCreateBookRequestDto_ReturnBookDto() throws Exception {
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setTitle("Sample Book Title 1");
        bookRequestDto.setAuthor("Author 1");
        bookRequestDto.setIsbn("978-1234587693");
        bookRequestDto.setPrice(BigDecimal.valueOf(60));
        bookRequestDto.setDescription("Description 1");
        bookRequestDto.setCoverImage("https://example.com/book-cover1.jpg");
        bookRequestDto.setCategoriesId(Set.of(1L));

        BookDto bookResponseDto = new BookDto();
        bookResponseDto.setId(1L);
        bookResponseDto.setTitle(bookRequestDto.getTitle());
        bookResponseDto.setAuthor(bookRequestDto.getAuthor());
        bookResponseDto.setIsbn(bookRequestDto.getIsbn());
        bookResponseDto.setPrice(bookRequestDto.getPrice());
        bookResponseDto.setDescription(bookRequestDto.getDescription());
        bookResponseDto.setCoverImage(bookRequestDto.getCoverImage());
        bookResponseDto.setCategoriesId(bookRequestDto.getCategoriesId());

        String jsonRequest = objectMapper.writeValueAsString(bookRequestDto);
        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        BookDto actualBookDto = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        assertNotNull(actualBookDto);
        assertNotNull(actualBookDto.getId());
        EqualsBuilder.reflectionEquals(bookResponseDto, actualBookDto);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Test the 'deleteById' endpoint with a valid book ID")
    void deleteById_ValidBookId_ReturnVoid() throws Exception {
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setTitle("Sample Book Title 4");
        bookRequestDto.setAuthor("Author 4");
        bookRequestDto.setIsbn("978-1234567894");
        bookRequestDto.setPrice(BigDecimal.valueOf(16));
        bookRequestDto.setDescription("Description 4");
        bookRequestDto.setCoverImage("https://example.com/book-cover4.jpg");
        bookRequestDto.setCategoriesId(Set.of(2L));

        BookDto bookResponseDto = new BookDto();
        bookResponseDto.setId(2L);
        bookResponseDto.setTitle(bookRequestDto.getTitle());
        bookResponseDto.setAuthor(bookRequestDto.getAuthor());
        bookResponseDto.setIsbn(bookRequestDto.getIsbn());
        bookResponseDto.setPrice(bookRequestDto.getPrice());
        bookResponseDto.setDescription(bookRequestDto.getDescription());
        bookResponseDto.setCoverImage(bookRequestDto.getCoverImage());
        bookResponseDto.setCategoriesId(bookRequestDto.getCategoriesId());

        Long bookId = bookResponseDto.getId();
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
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setTitle("Sample Book Title 4");
        bookRequestDto.setAuthor("Author 4");
        bookRequestDto.setIsbn("978-1234567894");
        bookRequestDto.setPrice(BigDecimal.valueOf(16));
        bookRequestDto.setDescription("Description 4");
        bookRequestDto.setCoverImage("https://example.com/book-cover4.jpg");
        bookRequestDto.setCategoriesId(Set.of(2L));

        BookDto bookResponseDto = new BookDto();
        bookResponseDto.setId(2L);
        bookResponseDto.setTitle(bookRequestDto.getTitle());
        bookResponseDto.setAuthor(bookRequestDto.getAuthor());
        bookResponseDto.setIsbn(bookRequestDto.getIsbn());
        bookResponseDto.setPrice(bookRequestDto.getPrice());
        bookResponseDto.setDescription(bookRequestDto.getDescription());
        bookResponseDto.setCoverImage(bookRequestDto.getCoverImage());
        bookResponseDto.setCategoriesId(bookRequestDto.getCategoriesId());

        List<BookDto> bookDtoList = new ArrayList<>();
        bookDtoList.add(bookResponseDto);
        BookSearchParametersDto params = new BookSearchParametersDto(
                new String[]{bookRequestDto.getTitle()},
                new String[]{bookRequestDto.getAuthor()},
                new String[]{String.valueOf(bookRequestDto.getCategoriesId())});
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
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setTitle("Sample Book Title 3");
        bookRequestDto.setAuthor("Author 3");
        bookRequestDto.setIsbn("978-1234567893");
        bookRequestDto.setPrice(BigDecimal.valueOf(25));
        bookRequestDto.setDescription("Description 3");
        bookRequestDto.setCoverImage("https://example.com/book-cover3.jpg");
        bookRequestDto.setCategoriesId(Set.of(1L));

        BookDtoWithoutCategoryId bookDtoWithoutCategoryId = new BookDtoWithoutCategoryId();
        bookDtoWithoutCategoryId.setId(1L);
        bookDtoWithoutCategoryId.setTitle(bookRequestDto.getTitle());
        bookDtoWithoutCategoryId.setAuthor(bookRequestDto.getAuthor());
        bookDtoWithoutCategoryId.setIsbn(bookRequestDto.getIsbn());
        bookDtoWithoutCategoryId.setPrice(bookRequestDto.getPrice());
        bookDtoWithoutCategoryId.setDescription(bookRequestDto.getDescription());
        bookDtoWithoutCategoryId.setCoverImage(bookRequestDto.getCoverImage());

        List<BookDtoWithoutCategoryId> expectedBookDto = new ArrayList<>();
        expectedBookDto.add(bookDtoWithoutCategoryId);

        MvcResult result = mockMvc.perform(get("/books/1/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDtoWithoutCategoryId[] actualBookDtoWithoutCategoryId = objectMapper
                .readValue(result.getResponse()
                .getContentAsByteArray(), BookDtoWithoutCategoryId[].class);
        assertNotNull(actualBookDtoWithoutCategoryId);
        assertEquals(expectedBookDto, Arrays.stream(actualBookDtoWithoutCategoryId).toList());
    }
}
