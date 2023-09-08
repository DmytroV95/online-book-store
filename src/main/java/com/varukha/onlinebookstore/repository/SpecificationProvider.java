package com.varukha.onlinebookstore.repository;

import com.varukha.onlinebookstore.model.Book;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T> {
    String getKey();

    Specification<Book> getSpecification(String[] params);
}
