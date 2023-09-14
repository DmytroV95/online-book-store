package com.varukha.onlinebookstore.dto.book;

public record BookSearchParametersDto(String[] titles,
                                      String[] authors) {
}
