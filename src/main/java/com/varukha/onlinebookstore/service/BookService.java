package com.varukha.onlinebookstore.service;

import com.varukha.onlinebookstore.dto.BookDto;
import com.varukha.onlinebookstore.dto.BookSearchParametersDto;
import com.varukha.onlinebookstore.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    List<BookDto> getAll();

    BookDto getById(Long id);

    void deleteById(Long id);

    BookDto update(Long id, CreateBookRequestDto bookRequestDto);

    List<BookDto> search(BookSearchParametersDto params);
}
