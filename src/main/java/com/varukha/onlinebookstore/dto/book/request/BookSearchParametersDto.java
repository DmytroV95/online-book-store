package com.varukha.onlinebookstore.dto.book.request;

public record BookSearchParametersDto(String[] titles,
                                      String[] authors) {
}
