package com.example.librarycqrs.application.command;

import com.example.librarycqrs.application.mediator.Request;
import jakarta.validation.constraints.NotNull;

public record DeleteAuthorCommand(
        @NotNull Long authorId
) implements Request<Void> {
}
