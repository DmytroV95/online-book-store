package com.varukha.onlinebookstore.service;

import com.varukha.onlinebookstore.dto.BookDto;
import com.varukha.onlinebookstore.dto.BookSearchParametersDto;
import com.varukha.onlinebookstore.dto.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    List<BookDto> getAll(Pageable pageable);

    BookDto getById(Long id);

    void deleteById(Long id);

    BookDto update(Long id, CreateBookRequestDto bookRequestDto);

    List<BookDto> search(BookSearchParametersDto params);
}
