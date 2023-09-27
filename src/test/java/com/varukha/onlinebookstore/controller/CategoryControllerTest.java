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
import com.varukha.onlinebookstore.model.Category;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("""
            Test the 'save' new category endpoint
            with valid request parameters
             """)
    void save_ValidCategoryDtoRequestData_ReturnCategoryDto() throws Exception {
        CreateCategoryRequestDto categoryRequestDto = new CreateCategoryRequestDto();
        categoryRequestDto.setName("Historical Fiction");
        categoryRequestDto.setDescription("Historical Fiction category description");

        CategoryDto categoryDtoResponse = new CategoryDto();
        categoryDtoResponse.setId(4L);
        categoryDtoResponse.setName(categoryRequestDto.getName());
        categoryDtoResponse.setDescription(categoryRequestDto.getDescription());

        String jsonRequest = objectMapper.writeValueAsString(categoryRequestDto);
        MvcResult result = mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        CategoryDto actualCategoryDto = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryDto.class);
        assertNotNull(actualCategoryDto);
        assertNotNull(actualCategoryDto.getId());
        EqualsBuilder.reflectionEquals(categoryDtoResponse, actualCategoryDto);
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("""
            Test the 'getAll' endpoint with valid request parameters
            and pagination to retrieve all book categories.
            """)
    void getAll_ValidCategoryDtoRequestData_ReturnCategoryDto() throws Exception {
        Category categoryNovel = new Category();
        categoryNovel.setId(1L);
        categoryNovel.setName("Novel");
        categoryNovel.setDescription("Novel category description");

        CategoryDto categoryDtoNovelResponse = new CategoryDto();
        categoryDtoNovelResponse.setId(categoryNovel.getId());
        categoryDtoNovelResponse.setName(categoryNovel.getName());
        categoryDtoNovelResponse.setDescription(categoryNovel.getDescription());

        Category scienceFictionCategory = new Category();
        scienceFictionCategory.setId(2L);
        scienceFictionCategory.setName("Science Fiction");
        scienceFictionCategory.setDescription("Science Fiction category description");

        CategoryDto categoryDtoScienceFictionResponse = new CategoryDto();
        categoryDtoScienceFictionResponse.setId(scienceFictionCategory.getId());
        categoryDtoScienceFictionResponse.setName(scienceFictionCategory.getName());
        categoryDtoScienceFictionResponse.setDescription(scienceFictionCategory.getDescription());

        Category fantasyCategory = new Category();
        fantasyCategory.setId(3L);
        fantasyCategory.setName("Fantasy");
        fantasyCategory.setDescription("Fantasy category description");

        CategoryDto categoryDtoFantasyResponse = new CategoryDto();
        categoryDtoFantasyResponse.setId(fantasyCategory.getId());
        categoryDtoFantasyResponse.setName(fantasyCategory.getName());
        categoryDtoFantasyResponse.setDescription(fantasyCategory.getDescription());

        List<CategoryDto> expectedCategoryDto = new ArrayList<>();
        expectedCategoryDto.add(categoryDtoNovelResponse);
        expectedCategoryDto.add(categoryDtoScienceFictionResponse);
        expectedCategoryDto.add(categoryDtoFantasyResponse);

        MvcResult result = mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto[] actualCategoryDto = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), CategoryDto[].class);
        assertNotNull(actualCategoryDto);
        assertEquals(4, actualCategoryDto.length);
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("""
            Test the 'getById' endpoint with valid request parameters
            for retrieving a book category by its ID.
            """)
    void getById_ValidCategoryId_ReturnCategoryDto() throws Exception {
        Category categoryNovel = new Category();
        categoryNovel.setId(1L);
        categoryNovel.setName("Novel");
        categoryNovel.setDescription("Novel category description");

        CategoryDto categoryDtoNovelResponse = new CategoryDto();
        categoryDtoNovelResponse.setId(categoryNovel.getId());
        categoryDtoNovelResponse.setName(categoryNovel.getName());
        categoryDtoNovelResponse.setDescription(categoryNovel.getDescription());

        Long categoryId = categoryDtoNovelResponse.getId();
        MvcResult result = mockMvc.perform(get("/categories/" + categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actualCategoryDto = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryDto.class);
        assertNotNull(actualCategoryDto);
        assertNotNull(actualCategoryDto.getId());
        assertEquals(categoryDtoNovelResponse.getId(), actualCategoryDto.getId());
        EqualsBuilder.reflectionEquals(categoryDtoNovelResponse, actualCategoryDto);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("""
            Test the 'update' endpoint with valid request parameters
            to update book category data by ID
            """)
    @Query
    void update_ValidCategoryDtoRequestData_ReturnCategoryDto() throws Exception {
        Category fantasyCategory = new Category();
        fantasyCategory.setId(3L);
        fantasyCategory.setName("Biography");
        fantasyCategory.setDescription("Biography category description");

        CategoryDto categoryDtoFantasyResponse = new CategoryDto();
        categoryDtoFantasyResponse.setId(fantasyCategory.getId());
        categoryDtoFantasyResponse.setName(fantasyCategory.getName());
        categoryDtoFantasyResponse.setDescription(fantasyCategory.getDescription());

        Long categoryId = categoryDtoFantasyResponse.getId();
        String jsonRequest = objectMapper.writeValueAsString(fantasyCategory);
        MvcResult result = mockMvc.perform(put("/categories/" + categoryId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actualCategoryDto = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryDto.class);
        assertNotNull(actualCategoryDto);
        EqualsBuilder.reflectionEquals(categoryDtoFantasyResponse, actualCategoryDto);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Test the 'deleteById' endpoint with a valid book ID")
    void deleteById_ValidCategoryId_ReturnVoid() throws Exception {
        Category fantasyCategory = new Category();
        fantasyCategory.setId(4L);
        fantasyCategory.setName("Fantasy");
        fantasyCategory.setDescription("Fantasy category description");

        CategoryDto categoryDtoFantasyResponse = new CategoryDto();
        categoryDtoFantasyResponse.setId(fantasyCategory.getId());
        categoryDtoFantasyResponse.setName(fantasyCategory.getName());
        categoryDtoFantasyResponse.setDescription(fantasyCategory.getDescription());

        Long categoryId = categoryDtoFantasyResponse.getId();
        mockMvc.perform(delete("/categories/" + categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
