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
import com.varukha.onlinebookstore.model.Book;
import com.varukha.onlinebookstore.model.Category;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

    private static final Category VALID_CATEGORY_1
            = new Category();
    private static final Category VALID_CATEGORY_2
            = new Category();
    private static final Book VALID_BOOK_1 = new Book();
    private static final Book VALID_BOOK_2 = new Book();

    private static final Book VALID_BOOK_1_UPDATED = new Book();
    private static final CreateBookRequestDto REQUEST_DTO_BOOK_1
            = new CreateBookRequestDto();
    private static final CreateBookRequestDto REQUEST_DTO_BOOK_2
            = new CreateBookRequestDto();
    private static final CreateBookRequestDto REQUEST_DTO_BOOK_3
            = new CreateBookRequestDto();
    private static final CreateBookRequestDto REQUEST_DTO_NEW_BOOK_4
            = new CreateBookRequestDto();

    private static final BookDto BOOK_1_DTO = new BookDto();
    private static final BookDto BOOK_2_DTO = new BookDto();
    private static final BookDto BOOK_3_DTO = new BookDto();
    private static final BookDto NEW_BOOK_4_DTO = new BookDto();
    private static final BookDto BOOK_1_DTO_UPDATED = new BookDto();

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
        BOOK_1_DTO.setCategoriesId(REQUEST_DTO_BOOK_1.getCategoriesId());

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

        BOOK_2_DTO.setId(2L);
        BOOK_2_DTO.setTitle(REQUEST_DTO_BOOK_2.getTitle());
        BOOK_2_DTO.setAuthor(REQUEST_DTO_BOOK_2.getAuthor());
        BOOK_2_DTO.setIsbn(REQUEST_DTO_BOOK_2.getIsbn());
        BOOK_2_DTO.setPrice(REQUEST_DTO_BOOK_2.getPrice());
        BOOK_2_DTO.setDescription(REQUEST_DTO_BOOK_2.getDescription());
        BOOK_2_DTO.setCoverImage(REQUEST_DTO_BOOK_2.getCoverImage());
        BOOK_2_DTO.setCategoriesId(REQUEST_DTO_BOOK_2.getCategoriesId());

        REQUEST_DTO_BOOK_3.setTitle("Sample Book Title 3");
        REQUEST_DTO_BOOK_3.setAuthor("Author 3");
        REQUEST_DTO_BOOK_3.setIsbn("978-1334567893");
        REQUEST_DTO_BOOK_3.setPrice(BigDecimal.valueOf(160));
        REQUEST_DTO_BOOK_3.setDescription("Description 3");
        REQUEST_DTO_BOOK_3.setCoverImage("https://example.com/book-cover3.jpg");
        REQUEST_DTO_BOOK_3.setCategoriesId(Set.of(VALID_CATEGORY_2.getId()));

        BOOK_3_DTO.setId(3L);
        BOOK_3_DTO.setTitle(REQUEST_DTO_BOOK_3.getTitle());
        BOOK_3_DTO.setAuthor(REQUEST_DTO_BOOK_3.getAuthor());
        BOOK_3_DTO.setIsbn(REQUEST_DTO_BOOK_3.getIsbn());
        BOOK_3_DTO.setPrice(REQUEST_DTO_BOOK_3.getPrice());
        BOOK_3_DTO.setDescription(REQUEST_DTO_BOOK_3.getDescription());
        BOOK_3_DTO.setCoverImage(REQUEST_DTO_BOOK_3.getCoverImage());
        BOOK_3_DTO.setCategoriesId(REQUEST_DTO_BOOK_3.getCategoriesId());

        REQUEST_DTO_NEW_BOOK_4.setTitle("Sample Book Title 4");
        REQUEST_DTO_NEW_BOOK_4.setAuthor("Author 4");
        REQUEST_DTO_NEW_BOOK_4.setIsbn("978-1334567894");
        REQUEST_DTO_NEW_BOOK_4.setPrice(BigDecimal.valueOf(200));
        REQUEST_DTO_NEW_BOOK_4.setDescription("Description 4");
        REQUEST_DTO_NEW_BOOK_4.setCoverImage("https://example.com/book-cover4.jpg");
        REQUEST_DTO_NEW_BOOK_4.setCategoriesId(Set.of(VALID_CATEGORY_1.getId()));

        NEW_BOOK_4_DTO.setId(4L);
        NEW_BOOK_4_DTO.setTitle(REQUEST_DTO_NEW_BOOK_4.getTitle());
        NEW_BOOK_4_DTO.setAuthor(REQUEST_DTO_NEW_BOOK_4.getAuthor());
        NEW_BOOK_4_DTO.setIsbn(REQUEST_DTO_NEW_BOOK_4.getIsbn());
        NEW_BOOK_4_DTO.setPrice(REQUEST_DTO_NEW_BOOK_4.getPrice());
        NEW_BOOK_4_DTO.setDescription(REQUEST_DTO_NEW_BOOK_4.getDescription());
        NEW_BOOK_4_DTO.setCoverImage(REQUEST_DTO_NEW_BOOK_4.getCoverImage());
        NEW_BOOK_4_DTO.setCategoriesId(REQUEST_DTO_NEW_BOOK_4.getCategoriesId());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("""
            Test the 'getAll' endpoint with valid request parameters
            and pagination to retrieve all books.
            """)
    void getAll_ValidBookData_ReturnAllBookDtoList() throws Exception {
        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        List<BookDto> actualBookDto = objectMapper.readValue(result.getResponse()
                        .getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class,
                        BookDto.class));
        assertNotNull(actualBookDto);
        assertEquals(3, actualBookDto.size());
        assertEquals(BOOK_1_DTO, actualBookDto.get(0));
        assertEquals(BOOK_2_DTO, actualBookDto.get(1));
        assertEquals(BOOK_3_DTO, actualBookDto.get(2));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("""
            Test the 'getById' endpoint with valid request parameters
            to retrieve a book by its ID
            """)
    void getById_ValidBookId_ShouldReturnValidBookData() throws Exception {
        MvcResult result = mockMvc.perform(get("/books/" + VALID_BOOK_1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto actualBookDto = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        assertNotNull(actualBookDto);
        assertNotNull(actualBookDto.getId());
        assertEquals(BOOK_1_DTO.getId(), actualBookDto.getId());
        EqualsBuilder.reflectionEquals(BOOK_1_DTO, actualBookDto);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("""
            Test the 'save' endpoint to add new book with
            valid request parameters
            """)
    void save_WithValidCreateBookRequestDto_ReturnBookDto() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(REQUEST_DTO_NEW_BOOK_4);
        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        BookDto actualBookDto = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        assertNotNull(actualBookDto);
        assertNotNull(actualBookDto.getId());
        assertEquals(NEW_BOOK_4_DTO.getId(), actualBookDto.getId());
        EqualsBuilder.reflectionEquals(NEW_BOOK_4_DTO, actualBookDto);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Test the 'deleteById' endpoint with a valid book ID")
    void deleteById_ValidBookId_ReturnVoid() throws Exception {
        mockMvc.perform(delete("/books/" + VALID_BOOK_2.getId())
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
        String jsonRequest = objectMapper.writeValueAsString(BOOK_1_DTO_UPDATED);
        MvcResult result = mockMvc.perform(put("/books/" + VALID_BOOK_1.getId())
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto actualBookDto = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        assertNotNull(actualBookDto);
        EqualsBuilder.reflectionEquals(BOOK_1_DTO_UPDATED, actualBookDto);
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("""
            Test the 'search' endpoint with valid input parameters.
            The method should return all books matching the specified input parameters
            """)
    void search_ValidParameters() throws Exception {
        BookSearchParametersDto params = new BookSearchParametersDto(
                new String[]{REQUEST_DTO_BOOK_1.getTitle()},
                new String[]{REQUEST_DTO_BOOK_1.getAuthor()},
                new String[]{String.valueOf(REQUEST_DTO_BOOK_1.getCategoriesId())});
        MvcResult result = mockMvc.perform(get("/books/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("titles", params.titles())
                        .param("authors", params.authors())
                        .param("category", params.categories()))
                .andExpect(status().isOk())
                .andReturn();
        List<BookDto> actualBookDto = objectMapper.readValue(result.getResponse()
                        .getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class,
                        BookDto.class));
        assertNotNull(actualBookDto);
        EqualsBuilder.reflectionEquals(BOOK_1_DTO, actualBookDto.get(0));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("""
            Test the 'getByCategoryId' endpoint with a valid category.
            The method should return all books matching the specified book category ID
            """)
    void getByCategoryId_ValidCategoryId_ReturnBookDtoWithoutCategoryId() throws Exception {
        MvcResult result = mockMvc.perform(get("/books/2/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDtoWithoutCategoryId[] actualBookDtoWithoutCategoryId = objectMapper
                .readValue(result.getResponse()
                        .getContentAsByteArray(), BookDtoWithoutCategoryId[].class);
        assertNotNull(actualBookDtoWithoutCategoryId);
        EqualsBuilder.reflectionEquals(VALID_BOOK_2,
                Arrays.stream(actualBookDtoWithoutCategoryId).toList().get(0));
    }
}
