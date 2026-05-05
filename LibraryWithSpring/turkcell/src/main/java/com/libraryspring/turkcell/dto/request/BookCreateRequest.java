package com.libraryspring.turkcell.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

/**
 * BookCreateRequest - Kitap oluşturmak için istek DTO'su
 * Validation: Başlık ve yazarId zorunlu, yayın yılı 0'dan büyük olmalı
 */
public record BookCreateRequest(
    @NotBlank(message = "Book title cannot be blank") String title,
    String isbn,
    @NotNull(message = "Published year cannot be null")
    @Min(value = 1000, message = "Published year must be valid") Integer publishedYear,
    Integer pageCount,
    @NotNull(message = "Author ID cannot be null") Long authorId,
    Long publisherId,
    Long categoryId
) {}
