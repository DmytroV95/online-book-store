package com.varukha.onlinebookstore.repository;

import com.varukha.onlinebookstore.dto.book.BookSearchParametersDto;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> build(BookSearchParametersDto searchParametersDto);
}
