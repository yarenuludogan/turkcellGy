package com.example.librarycqrs.application.command;

import com.example.librarycqrs.application.mediator.Request;
import jakarta.validation.constraints.NotBlank;

public record CreateAuthorCommand(
        @NotBlank String name
) implements Request<Long> {
}
