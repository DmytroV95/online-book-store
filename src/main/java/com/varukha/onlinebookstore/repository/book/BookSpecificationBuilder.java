package com.varukha.onlinebookstore.repository.book;

import com.varukha.onlinebookstore.dto.book.BookSearchParametersDto;
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
        spec = getBookSpecification(searchParametersDto.titles(), spec, TITLE_KEY);
        spec = getBookSpecification(searchParametersDto.authors(), spec, AUTHOR_KEY);
        return spec;
    }

    private Specification<Book> getBookSpecification(String[] searchParametersDto,
                                                     Specification<Book> spec,
                                                     String key) {
        if (searchParametersDto != null && searchParametersDto.length > 0) {
            spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider(key)
                    .getSpecification(searchParametersDto));
        }
        return spec;
    }
}
