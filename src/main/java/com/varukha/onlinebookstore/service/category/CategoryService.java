package com.varukha.onlinebookstore.service.category;

import com.varukha.onlinebookstore.dto.category.CategoryDto;
import com.varukha.onlinebookstore.dto.category.CreateCategoryRequestDto;
import com.varukha.onlinebookstore.model.Category;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CategoryDto requestDto);

    CategoryDto update(Long id, CategoryDto categoryDto);

    void deleteById(Long id);
}
