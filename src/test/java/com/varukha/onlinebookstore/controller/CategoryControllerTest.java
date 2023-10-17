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
import com.varukha.onlinebookstore.dto.category.CategoryDto;
import com.varukha.onlinebookstore.dto.category.CreateCategoryRequestDto;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
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
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {
    protected static MockMvc mockMvc;
    private static final CreateCategoryRequestDto VALID_CREATE_REQUEST_DTO_1 =
            new CreateCategoryRequestDto();
    private static final CreateCategoryRequestDto VALID_CREATE_REQUEST_DTO_2 =
            new CreateCategoryRequestDto();
    private static final CreateCategoryRequestDto VALID_CREATE_REQUEST_DTO_3 =
            new CreateCategoryRequestDto();
    private static final CreateCategoryRequestDto VALID_CREATE_REQUEST_DTO_4 =
            new CreateCategoryRequestDto();
    private static final CategoryDto VALID_CATEGORY_DTO_1 = new CategoryDto();
    private static final CategoryDto VALID_CATEGORY_DTO_2 = new CategoryDto();
    private static final CategoryDto VALID_CATEGORY_DTO_3 = new CategoryDto();
    private static final CategoryDto VALID_CATEGORY_DTO_4 = new CategoryDto();
    private static final CategoryDto VALID_GET_RESPONSE = new CategoryDto();
    private static final CreateCategoryRequestDto VALID_UPDATE_REQUEST_DTO =
            new CreateCategoryRequestDto();
    private static final CategoryDto VALID_UPDATE_DTO = new CategoryDto();

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
                            "database/category/add-category-to-category-table.sql")
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
                            "database/category/remove-category-from-category-table.sql")
            );
        }
    }

    @BeforeEach
    public void setupTestData() {
        VALID_CREATE_REQUEST_DTO_1.setName("Novel");
        VALID_CREATE_REQUEST_DTO_1.setDescription("Novel category description");

        VALID_CATEGORY_DTO_1.setId(1L);
        VALID_CATEGORY_DTO_1.setName(VALID_CREATE_REQUEST_DTO_1.getName());
        VALID_CATEGORY_DTO_1.setDescription(VALID_CREATE_REQUEST_DTO_1.getDescription());

        VALID_CREATE_REQUEST_DTO_2.setName("Science Fiction");
        VALID_CREATE_REQUEST_DTO_2.setDescription("Science Fiction category description");

        VALID_CATEGORY_DTO_2.setId(2L);
        VALID_CATEGORY_DTO_2.setName(VALID_CREATE_REQUEST_DTO_2.getName());
        VALID_CATEGORY_DTO_2.setDescription(VALID_CREATE_REQUEST_DTO_2.getDescription());

        VALID_CREATE_REQUEST_DTO_3.setName("Fantasy");
        VALID_CREATE_REQUEST_DTO_3.setDescription("Fantasy category description");

        VALID_CATEGORY_DTO_3.setId(3L);
        VALID_CATEGORY_DTO_3.setName(VALID_CREATE_REQUEST_DTO_3.getName());
        VALID_CATEGORY_DTO_3.setDescription(VALID_CREATE_REQUEST_DTO_3.getDescription());

        VALID_CREATE_REQUEST_DTO_4.setName("Historical Fiction");
        VALID_CREATE_REQUEST_DTO_4.setDescription("Historical Fiction category description");

        VALID_CATEGORY_DTO_4.setId(4L);
        VALID_CATEGORY_DTO_4.setName(VALID_CREATE_REQUEST_DTO_4.getName());
        VALID_CATEGORY_DTO_4.setDescription(VALID_CREATE_REQUEST_DTO_4.getDescription());

        VALID_GET_RESPONSE.setName("Science Fiction");
        VALID_GET_RESPONSE.setDescription("Science Fiction category description");

        VALID_UPDATE_REQUEST_DTO.setName("Updated title");
        VALID_UPDATE_REQUEST_DTO.setDescription("Updated description");

        VALID_UPDATE_DTO.setId(VALID_CATEGORY_DTO_3.getId());
        VALID_UPDATE_DTO.setName(VALID_UPDATE_REQUEST_DTO.getName());
        VALID_UPDATE_DTO.setDescription(VALID_UPDATE_REQUEST_DTO.getDescription());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("""
            Test the 'save' new category endpoint
            with valid request parameters
             """)
    void save_ValidCategoryDtoRequestData_ReturnCategoryDto() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(VALID_CREATE_REQUEST_DTO_4);
        MvcResult result = mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        CategoryDto actualCategoryDto = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryDto.class);
        assertNotNull(actualCategoryDto);
        EqualsBuilder.reflectionEquals(VALID_CATEGORY_DTO_4,
                actualCategoryDto,
                "id");
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("""
            Test the 'getAll' endpoint with valid request parameters
            and pagination to retrieve all book categories.
            """)
    void getAll_ValidCategoryDtoRequestData_ReturnCategoryDto() throws Exception {
        List<CategoryDto> expected = List.of(VALID_CATEGORY_DTO_1, VALID_CATEGORY_DTO_2,
                VALID_UPDATE_DTO, VALID_CATEGORY_DTO_4);
        MvcResult result = mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto[] actualCategoryDto = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), CategoryDto[].class);
        assertNotNull(actualCategoryDto);
        assertEquals(4, actualCategoryDto.length);
        assertEquals(expected, Arrays.stream(actualCategoryDto).toList());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("""
            Test the 'getById' endpoint with valid request parameters
            for retrieving a book category by its ID.
            """)
    void getById_ValidCategoryId_ReturnCategoryDto() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actualCategoryDto = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryDto.class);
        assertNotNull(actualCategoryDto);
        assertNotNull(actualCategoryDto.getId());
        EqualsBuilder.reflectionEquals(VALID_GET_RESPONSE,
                actualCategoryDto,
                "id");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("""
            Test the 'update' endpoint with valid request parameters
            to update book category data by ID
            """)
    @Query
    void update_ValidCategoryDtoRequestData_ReturnCategoryDto() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(VALID_UPDATE_REQUEST_DTO);
        MvcResult result = mockMvc.perform(put("/categories/3")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actualCategoryDto = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryDto.class);
        assertNotNull(actualCategoryDto);
        EqualsBuilder.reflectionEquals(VALID_UPDATE_DTO,
                actualCategoryDto, "id");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Test the 'deleteById' endpoint with a valid book ID")
    void deleteById_ValidCategoryId_ReturnVoid() throws Exception {
        mockMvc.perform(delete("/categories/4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
