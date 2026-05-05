package com.libraryspring.turkcell.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * LoanCreateRequest - Ödünçleme oluşturmak için istek DTO'su
 * Validation: Kopya, öğrenci ve tarihler zorunlu
 */
public record LoanCreateRequest(
    @NotNull(message = "Book copy ID cannot be null") Long bookCopyId,
    @NotNull(message = "Student ID cannot be null") Long studentId,
    Long staffId,
    @NotNull(message = "Loan date cannot be null") LocalDate loanDate,
    @NotNull(message = "Due date cannot be null") LocalDate dueDate
) {}
