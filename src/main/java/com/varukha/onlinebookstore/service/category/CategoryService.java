package com.varukha.onlinebookstore.service.category;

import com.varukha.onlinebookstore.dto.category.CategoryDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryDto> getAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CategoryDto requestDto);

    CategoryDto update(Long id, CategoryDto categoryDto);

    void deleteById(Long id);
}
