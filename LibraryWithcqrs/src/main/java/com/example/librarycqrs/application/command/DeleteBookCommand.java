package com.example.librarycqrs.application.command;

import com.example.librarycqrs.application.mediator.Request;
import jakarta.validation.constraints.NotNull;

public record DeleteBookCommand(
        @NotNull Long bookId
) implements Request<Void> {
}
