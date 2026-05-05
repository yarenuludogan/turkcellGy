package com.libraryspring.turkcell.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * CategoryCreateRequest - Kategori oluşturmak için istek DTO'su
 */
public record CategoryCreateRequest(
    @NotBlank(message = "Category name cannot be blank") String name
) {}
