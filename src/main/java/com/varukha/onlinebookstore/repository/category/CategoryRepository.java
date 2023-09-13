package com.varukha.onlinebookstore.repository.category;

import com.varukha.onlinebookstore.model.Book;
import com.varukha.onlinebookstore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
