package com.example.librarycqrs.application.command;

import com.example.librarycqrs.application.mediator.Request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateBookCommand(
        @NotNull Long bookId,
        @NotBlank String title,
        @NotBlank String isbn,
        @NotNull Integer publishedYear,
        @NotNull Long authorId
) implements Request<Long> {
}
