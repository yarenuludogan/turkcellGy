package com.example.librarycqrs.web.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateAuthorRequest(
        @NotBlank String name
) {
}
