package com.varukha.onlinebookstore.service;

import com.varukha.onlinebookstore.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
