package com.varukha.onlinebookstore.controller;

import com.varukha.onlinebookstore.dto.book.BookDto;
import com.varukha.onlinebookstore.dto.book.BookSearchParametersDto;
import com.varukha.onlinebookstore.dto.book.CreateBookRequestDto;
import com.varukha.onlinebookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book management", description = "Endpoints for managing books")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @Operation(summary = "Get all books",
            description = "Get list of all available books")
    public List<BookDto> getAll(Pageable pageable) {
        return bookService.getAll(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @Operation(summary = "Get the book by id",
            description = "Get the existed book by identification number")
    public BookDto getById(@PathVariable Long id) {
        return bookService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Save a new book",
            description = "Save a new book")
    public BookDto save(@RequestBody @Valid CreateBookRequestDto bookDto) {
        return bookService.save(bookDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Delete the book by id",
            description = "Delete the existed book by identification number")
    public void deleteById(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Update the book by id",
            description = "Update the existed book information by identification number")
    public BookDto update(@PathVariable Long id,
                          @Valid @RequestBody CreateBookRequestDto bookRequestDto) {
        return bookService.update(id, bookRequestDto);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @Operation(summary = "Get a book by searching parameters",
            description = "Search a book by input parameters")
    public List<BookDto> search(BookSearchParametersDto searchParameters) {
        return bookService.search(searchParameters);
    }
}
