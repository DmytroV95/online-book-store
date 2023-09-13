package com.varukha.onlinebookstore.controller;

import com.varukha.onlinebookstore.dto.category.CategoryDto;
import com.varukha.onlinebookstore.model.Book;
import com.varukha.onlinebookstore.service.category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CategoryDto save(@RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public List<CategoryDto> getAll(Pageable pageable) {
        return categoryService.getAll(pageable);
    }

    public CategoryDto getCategoryById(Long id) {
        return null;
    }

    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        return null;
    }

    public void deleteCategory(Long id) {

    }

}
