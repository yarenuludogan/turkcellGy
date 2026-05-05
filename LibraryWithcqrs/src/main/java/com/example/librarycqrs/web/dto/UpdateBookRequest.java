package com.example.librarycqrs.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateBookRequest(
        @NotBlank String title,
        @NotBlank String isbn,
        @NotNull Integer publishedYear,
        @NotNull Long authorId
) {
}
