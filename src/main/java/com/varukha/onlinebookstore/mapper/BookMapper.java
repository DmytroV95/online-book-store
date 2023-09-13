package com.varukha.onlinebookstore.mapper;

import com.varukha.onlinebookstore.config.MapperConfig;
import com.varukha.onlinebookstore.dto.book.response.BookDto;
import com.varukha.onlinebookstore.dto.book.response.BookDtoWithoutCategoryId;
import com.varukha.onlinebookstore.dto.book.request.CreateBookRequestDto;
import com.varukha.onlinebookstore.model.Book;
import com.varukha.onlinebookstore.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.stream.Collectors;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categoriesId", ignore = true)
    BookDto toDto(Book book);

    @Mapping(target = "categories", ignore = true)
    Book toModel(CreateBookRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    BookDtoWithoutCategoryId toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        if (book.getCategories() != null) {
            bookDto.setCategoriesId(book.getCategories().stream()
                    .map(Category::getId)
                    .collect(Collectors.toSet()));
        }
    }

    @AfterMapping
    default void setCategories(@MappingTarget Book book,
                               CreateBookRequestDto bookRequestDto) {
        book.setCategories(bookRequestDto
                .getCategoriesId()
                .stream()
                .map(Category::new)
                .collect(Collectors.toSet()));
    }
}
