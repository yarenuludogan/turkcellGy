package com.example.librarycqrs.application.view;

public record BookView(
        Long id,
        String title,
        String isbn,
        Integer publishedYear,
        Long authorId,
        String authorName
) {
}
