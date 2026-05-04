package com.example.librarycqrs.web.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateAuthorRequest(
        @NotBlank String name
) {
}
