package com.varukha.onlinebookstore.service.impl;

import com.varukha.onlinebookstore.dto.BookDto;
import com.varukha.onlinebookstore.dto.CreateBookRequestDto;
import com.varukha.onlinebookstore.mapper.BookMapper;
import com.varukha.onlinebookstore.model.Book;
import com.varukha.onlinebookstore.repository.BookRepository;
import com.varukha.onlinebookstore.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> getAll() {
        return bookRepository.getAll()
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findBookById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id: " + id)
        );
        return bookMapper.toDto(book);
    }
}
