package com.varukha.onlinebookstore.repository;

import com.varukha.onlinebookstore.model.Book;
import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book save(Book book);

    List<Book> getAll();

    Optional<Book> findBookById(Long id);
}
