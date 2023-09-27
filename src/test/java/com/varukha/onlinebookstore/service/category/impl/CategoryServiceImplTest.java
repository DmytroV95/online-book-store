package com.varukha.onlinebookstore.service.category.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.varukha.onlinebookstore.dto.category.CategoryDto;
import com.varukha.onlinebookstore.exception.EntityNotFoundException;
import com.varukha.onlinebookstore.mapper.CategoryMapper;
import com.varukha.onlinebookstore.model.Category;
import com.varukha.onlinebookstore.repository.category.CategoryRepository;
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

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @Test
    @DisplayName("""
            Test the 'getAll' method with valid request parameters
            and pagination to retrieve all book categories.
            """)
    void getAll_ValidCategoryDtoRequestData_ReturnCategoryDto() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Novel");
        category.setDescription("Novel category description.");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(categoryDto.getName());
        categoryDto.setDescription(categoryDto.getDescription());

        Pageable pageable = PageRequest.of(0, 10);
        List<Category> categories = Collections.singletonList(category);

        when(categoryRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(categories));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        List<CategoryDto> categoryDtoList = categoryService.getAll(pageable);

        assertNotNull(categoryDtoList);
        assertThat(categoryDtoList).hasSize(1);
        assertThat(categoryDtoList.get(0)).isEqualTo(categoryDto);
    }

    @Test
    @DisplayName("""
            Test the 'getById' method with valid request parameters
            for retrieving a book category by its ID.
            """)
    void getById_ValidCategoryId_ReturnCategoryDto() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Novel");
        category.setDescription("Novel category description.");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(categoryDto.getName());
        categoryDto.setDescription(categoryDto.getDescription());

        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.getById(categoryId);

        assertNotNull(result);
        assertEquals(categoryDto, result);
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
        Category category = new Category();
        category.setId(1L);
        category.setName("Novel");
        category.setDescription("Novel category description.");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(categoryDto.getName());
        categoryDto.setDescription(categoryDto.getDescription());

        when(categoryMapper.toModel(categoryDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.save(categoryDto);

        assertNotNull(result);
        assertThat(result).isEqualTo(categoryDto);
    }

    @Test
    @DisplayName("""
            Test the 'update' method with valid request parameters
            to update book category data by ID
            """)
    void update_ValidCategoryDtoRequestData_ReturnCategoryDto() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Novel");
        category.setDescription("Novel category description.");

        Category updatedCategory = new Category();
        category.setName("Science Fiction");
        category.setDescription("Science Fiction category description.");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(updatedCategory.getName());
        categoryDto.setDescription(updatedCategory.getDescription());

        when(categoryMapper.toModel(categoryDto)).thenReturn(updatedCategory);
        when(categoryRepository.save(updatedCategory)).thenReturn(updatedCategory);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(categoryDto);

        Long categoryId = 1L;
        CategoryDto result = categoryService.update(categoryId, categoryDto);
        assertNotNull(result);
        assertEquals(categoryDto, result);
    }

    @Test
    @DisplayName("Test the 'deleteById' method with a valid book ID")
    void deleteById_ValidCategoryId_ReturnVoid() {
        Long categoryId = 1L;
        categoryService.deleteById(categoryId);
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }
}
