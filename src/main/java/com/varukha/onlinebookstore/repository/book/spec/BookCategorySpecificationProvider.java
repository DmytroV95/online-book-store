package com.varukha.onlinebookstore.repository.book.spec;

import static com.varukha.onlinebookstore.repository.book.BookSpecificationBuilder.CATEGORIES_ID_KEY;

import com.varukha.onlinebookstore.model.Book;
import com.varukha.onlinebookstore.model.Category;
import com.varukha.onlinebookstore.repository.SpecificationProvider;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class BookCategorySpecificationProvider implements SpecificationProvider<Book> {
    private static final String BOOK_CATEGORY_FIELD_NAME = "categories";

    @Override
    public String getKey() {
        return CATEGORIES_ID_KEY;
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> {
            Join<Book, Category> categoryJoin = root.join(BOOK_CATEGORY_FIELD_NAME, JoinType.LEFT);
            return criteriaBuilder.and(categoryJoin.get(CATEGORIES_ID_KEY)
                    .in(Arrays.stream(params).toArray()));
        };
    }
}
