package com.varukha.onlinebookstore.repository.book;

import com.varukha.onlinebookstore.dto.book.request.BookSearchParametersDto;
import com.varukha.onlinebookstore.model.Book;
import com.varukha.onlinebookstore.repository.SpecificationBuilder;
import com.varukha.onlinebookstore.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    public static final String TITLE_KEY = "title";
    public static final String AUTHOR_KEY = "author";
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParametersDto) {
        Specification<Book> spec = Specification.where(null);
        if (searchParametersDto.titles() != null
                && searchParametersDto.titles().length > 0) {
            spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider(TITLE_KEY)
                    .getSpecification(searchParametersDto.titles()));
        }
        if (searchParametersDto.authors() != null
                && searchParametersDto.authors().length > 0) {
            spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider(AUTHOR_KEY)
                    .getSpecification(searchParametersDto.authors()));
        }
        return spec;
    }
}
