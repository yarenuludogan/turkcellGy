package com.libraryspring.turkcell.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * ReturnTableCreateRequest - Iade işlemi oluşturmak için istek DTO'su
 */
public record ReturnTableCreateRequest(
    @NotNull(message = "Loan ID cannot be null") Long loanId,
    @NotNull(message = "Return date cannot be null") LocalDate returnDate,
    String conditionNotes
) {}
