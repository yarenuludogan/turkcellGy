package com.example.librarycqrs.application.command;

import com.example.librarycqrs.application.mediator.Request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateAuthorCommand(
        @NotNull Long authorId,
        @NotBlank String name
) implements Request<Long> {
}
