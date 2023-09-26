package com.varukha.onlinebookstore.service.category.impl;

import com.varukha.onlinebookstore.dto.category.CategoryDto;
import com.varukha.onlinebookstore.exception.EntityNotFoundException;
import com.varukha.onlinebookstore.mapper.CategoryMapper;
import com.varukha.onlinebookstore.model.Category;
import com.varukha.onlinebookstore.repository.category.CategoryRepository;
import com.varukha.onlinebookstore.service.category.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find category by id: " + id)
        );
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto save(CategoryDto requestDto) {
        Category category = categoryMapper.toModel(requestDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto update(Long id, CategoryDto categoryDto) {
        Category updatedCategory = categoryMapper.toModel(categoryDto);
        updatedCategory.setId(id);
        return categoryMapper.toDto(categoryRepository.save(updatedCategory));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
