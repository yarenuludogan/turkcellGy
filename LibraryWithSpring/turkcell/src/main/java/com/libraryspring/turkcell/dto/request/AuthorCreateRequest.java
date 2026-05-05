package com.libraryspring.turkcell.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * AuthorCreateRequest - Yazar oluşturmak için istek DTO'su
 * @NotBlank 
 */
public record AuthorCreateRequest(
    @NotBlank(message = "Author name cannot be blank") String name,
    String country
) {}
