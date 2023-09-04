package com.varukha.onlinebookstore.service;

import com.varukha.onlinebookstore.dto.BookDto;
import com.varukha.onlinebookstore.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    List<BookDto> getAll();

    BookDto getBookById(Long id);
}
