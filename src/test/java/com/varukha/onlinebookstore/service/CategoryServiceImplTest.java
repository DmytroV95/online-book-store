package com.varukha.onlinebookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.varukha.onlinebookstore.dto.category.CategoryDto;
import com.varukha.onlinebookstore.exception.EntityNotFoundException;
import com.varukha.onlinebookstore.mapper.CategoryMapper;
import com.varukha.onlinebookstore.model.Category;
import com.varukha.onlinebookstore.repository.category.CategoryRepository;
import com.varukha.onlinebookstore.service.category.impl.CategoryServiceImpl;
import java.util.List;
import java.util.Optional;
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

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    private static final Category VALID_CATEGORY_1 = new Category();
    private static final Category VALID_CATEGORY_2 = new Category();
    private static final Category UPADATED_CATEGORY = new Category();
    private static final CategoryDto CATEGORY_DTO_1 = new CategoryDto();
    private static final CategoryDto CATEGORY_DTO_2 = new CategoryDto();
    private static final CategoryDto UPDATED_CATEGORY_DTO = new CategoryDto();

    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @BeforeEach
    void setUp() {
        VALID_CATEGORY_1.setId(1L);
        VALID_CATEGORY_1.setName("Science Fiction");
        VALID_CATEGORY_1.setDescription("Science Fiction category description");

        CATEGORY_DTO_1.setId(VALID_CATEGORY_1.getId());
        CATEGORY_DTO_1.setName(VALID_CATEGORY_1.getName());
        CATEGORY_DTO_1.setDescription(VALID_CATEGORY_1.getDescription());

        VALID_CATEGORY_2.setId(2L);
        VALID_CATEGORY_2.setName("Mystery");
        VALID_CATEGORY_2.setDescription("Mystery category description");

        CATEGORY_DTO_2.setId(VALID_CATEGORY_2.getId());
        CATEGORY_DTO_2.setName(VALID_CATEGORY_2.getName());
        CATEGORY_DTO_2.setDescription(VALID_CATEGORY_2.getDescription());

        UPADATED_CATEGORY.setName("Fantasy");
        UPADATED_CATEGORY.setDescription("Fantasy category description");

        UPDATED_CATEGORY_DTO.setId(VALID_CATEGORY_1.getId());
        UPDATED_CATEGORY_DTO.setName(UPADATED_CATEGORY.getName());
        UPDATED_CATEGORY_DTO.setDescription(UPADATED_CATEGORY.getDescription());
    }

    @Test
    @DisplayName("""
            Test the 'getAll' method with valid request parameters
            and pagination to retrieve all book categories.
            """)
    void getAll_ValidCategoryDtoRequestData_ReturnCategoryDto() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Category> expectedCategoryList = List.of(VALID_CATEGORY_1, VALID_CATEGORY_2);

        when(categoryRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(expectedCategoryList));
        when(categoryMapper.toDto(VALID_CATEGORY_1)).thenReturn(CATEGORY_DTO_1);
        when(categoryMapper.toDto(VALID_CATEGORY_2)).thenReturn(CATEGORY_DTO_2);

        List<CategoryDto> categoryDtoList = categoryService.getAll(pageable);

        assertNotNull(categoryDtoList);
        assertEquals(2, categoryDtoList.size());
        assertEquals(CATEGORY_DTO_1, categoryDtoList.get(0));
        assertEquals(CATEGORY_DTO_2, categoryDtoList.get(1));
    }

    @Test
    @DisplayName("""
            Test the 'getById' method with valid request parameters
            for retrieving a book category by its ID.
            """)
    void getById_ValidCategoryId_ReturnCategoryDto() {
        Long categoryId = VALID_CATEGORY_1.getId();
        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(VALID_CATEGORY_1));
        when(categoryMapper.toDto(VALID_CATEGORY_1)).thenReturn(CATEGORY_DTO_1);

        CategoryDto result = categoryService.getById(categoryId);

        assertNotNull(result);
        assertEquals(CATEGORY_DTO_1, result);
    }

    @Test
    @DisplayName("""
            Test the 'getById' method with a non-existent book category ID.
            The method should throw an EntityNotFoundException
            """)
    void getById_WithNonExistedCategoryId_ShouldThrowException() {
        Long categoryId = 100L;
        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(categoryId));
        String expected = "Can't find category by id: " + categoryId;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Test the 'save' new book category method
            with valid request parameters
             """)
    void save_ValidCategoryDtoRequestData_ReturnCategoryDto() {
        when(categoryMapper.toModel(CATEGORY_DTO_1)).thenReturn(VALID_CATEGORY_1);
        when(categoryRepository.save(VALID_CATEGORY_1)).thenReturn(VALID_CATEGORY_1);
        when(categoryMapper.toDto(VALID_CATEGORY_1)).thenReturn(CATEGORY_DTO_1);

        CategoryDto result = categoryService.save(CATEGORY_DTO_1);

        assertNotNull(result);
        assertThat(result).isEqualTo(CATEGORY_DTO_1);
    }

    @Test
    @DisplayName("""
            Test the 'update' method with valid request parameters
            to update book category data by ID
            """)
    void update_ValidCategoryDtoRequestData_ReturnCategoryDto() {
        when(categoryMapper.toModel(UPDATED_CATEGORY_DTO)).thenReturn(UPADATED_CATEGORY);
        when(categoryRepository.save(UPADATED_CATEGORY)).thenReturn(UPADATED_CATEGORY);
        when(categoryMapper.toDto(UPADATED_CATEGORY)).thenReturn(UPDATED_CATEGORY_DTO);

        Long categoryId = UPADATED_CATEGORY.getId();
        CategoryDto result = categoryService.update(categoryId, UPDATED_CATEGORY_DTO);
        assertNotNull(result);
        assertEquals(UPDATED_CATEGORY_DTO, result);
    }

    @Test
    @DisplayName("Test the 'deleteById' method with a valid book ID")
    void deleteById_ValidCategoryId_ReturnVoid() {
        Long categoryId = 1L;
        categoryService.deleteById(categoryId);
        verify(categoryRepository).deleteById(categoryId);
    }
}
