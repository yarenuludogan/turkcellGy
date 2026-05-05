package com.libraryspring.turkcell.dto.response;

import com.libraryspring.turkcell.entity.ReturnTable;
import java.time.LocalDate;

/**
 * ReturnTableResponse - Iade işlemi bilgilerini döndürmek için response DTO'su
 */
public record ReturnTableResponse(
    Long returnId,
    Long loanId,           // Sadece ödünçleme ID'si
    LocalDate returnDate,
    String conditionNotes
) {
    public static ReturnTableResponse fromEntity(ReturnTable returnTable) {
        return new ReturnTableResponse(
            returnTable.getReturnId(),
            returnTable.getLoan() != null ? returnTable.getLoan().getLoanId() : null,
            returnTable.getReturnDate(),
            returnTable.getConditionNotes()
        );
    }
}
