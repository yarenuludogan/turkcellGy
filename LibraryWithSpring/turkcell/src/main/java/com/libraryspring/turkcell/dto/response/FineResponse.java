package com.libraryspring.turkcell.dto.response;

import com.libraryspring.turkcell.entity.Fine;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * FineResponse - Ceza bilgilerini döndürmek için response DTO'su
 */
public record FineResponse(
    Long fineId,
    Long loanId,          
    BigDecimal amount,
    Boolean paid,
    LocalDate issueDate
) {
    public static FineResponse fromEntity(Fine fine) {
        return new FineResponse(
            fine.getFineId(),
            fine.getLoan() != null ? fine.getLoan().getLoanId() : null,
            fine.getAmount(),
            fine.getPaid(),
            fine.getIssueDate()
        );
    }
}
