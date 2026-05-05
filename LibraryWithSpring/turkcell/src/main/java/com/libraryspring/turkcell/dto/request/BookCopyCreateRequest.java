package com.libraryspring.turkcell.dto.request;

import jakarta.validation.constraints.NotNull;

/**
 * BookCopyCreateRequest - Kitap kopyası oluşturmak için istek DTO'su
 */
public record BookCopyCreateRequest(
    @NotNull(message = "Book ID cannot be null") Long bookId,
    String status,
    String shelfLocation
) {}
