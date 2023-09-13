package com.varukha.onlinebookstore.repository.book;

import com.varukha.onlinebookstore.dto.book.response.BookDtoWithoutCategoryId;
import com.varukha.onlinebookstore.model.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>,
        JpaSpecificationExecutor<Book> {
    @Query("FROM Book b JOIN b.categories c WHERE c.id = :categoryId")
    List<Book> findAllByCategoryId(Long categoryId);

    @Query("FROM Book b JOIN FETCH b.categories")
    List<Book> findAllWithCategory(Pageable pageable);

    @Query("FROM Book b JOIN FETCH b.categories WHERE b.id = :id")
    Optional<Book> findByIdWithCategory(Long id);


}
