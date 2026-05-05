package com.libraryspring.turkcell.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

/**
 * StudentCreateRequest - Öğrenci oluşturmak için istek DTO'su
 */
public record StudentCreateRequest(
    @NotBlank(message = "Student name cannot be blank") String name,
    @Email(message = "Email should be valid") String email,
    String phone
) {}
