package com.varukha.onlinebookstore.repository.book.spec;

import static com.varukha.onlinebookstore.repository.book.BookSpecificationBuilder.AUTHOR_KEY;

import com.varukha.onlinebookstore.model.Book;
import com.varukha.onlinebookstore.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return AUTHOR_KEY;
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> root.get(AUTHOR_KEY)
                .in(Arrays.stream(params).toArray());
    }
}
