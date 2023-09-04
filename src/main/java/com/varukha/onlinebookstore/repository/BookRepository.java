package com.varukha.onlinebookstore.repository;

import com.varukha.onlinebookstore.model.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
