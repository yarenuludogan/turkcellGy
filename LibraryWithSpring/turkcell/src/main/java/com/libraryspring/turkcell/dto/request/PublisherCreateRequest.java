package com.libraryspring.turkcell.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * PublisherCreateRequest - Yayıncı oluşturmak için istek DTO'su
 */
public record PublisherCreateRequest(
    @NotBlank(message = "Publisher name cannot be blank") String name,
    String address
) {}
