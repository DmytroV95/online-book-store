package com.varukha.onlinebookstore.service.book;

import com.varukha.onlinebookstore.dto.book.BookDto;
import com.varukha.onlinebookstore.dto.book.BookSearchParametersDto;
import com.varukha.onlinebookstore.dto.book.CreateBookRequestDto;
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
