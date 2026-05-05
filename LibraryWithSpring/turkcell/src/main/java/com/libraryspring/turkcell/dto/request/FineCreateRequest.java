package com.libraryspring.turkcell.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

/**
 * FineCreateRequest - Ceza oluşturmak için istek DTO'su
 */
public record FineCreateRequest(
    @NotNull(message = "Loan ID cannot be null") Long loanId,
    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0") BigDecimal amount,
    Boolean paid
) {}
