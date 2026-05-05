package com.libraryspring.turkcell.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * StaffCreateRequest - Personel oluşturmak için istek DTO'su
 */
public record StaffCreateRequest(
    @NotBlank(message = "Staff name cannot be blank") String name,
    String role,
    LocalDate hireDate
) {}
