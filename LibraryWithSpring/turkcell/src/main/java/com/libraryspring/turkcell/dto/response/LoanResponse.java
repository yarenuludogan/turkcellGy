package com.libraryspring.turkcell.dto.response;

import com.libraryspring.turkcell.entity.Loan;
import java.time.LocalDate;

/**
 * LoanResponse - Ödünçleme bilgilerini döndürmek için response DTO'su
 * İlişkileri ID'ler olarak döndürür
 */
public record LoanResponse(
    Long loanId,
    Long bookCopyId,       // BookCopy nesnesi değil, sadece ID
    Long studentId,        // Student nesnesi değil, sadece ID
    Long staffId,          // Staff nesnesi değil, sadece ID
    LocalDate loanDate,
    LocalDate dueDate
) {
    public static LoanResponse fromEntity(Loan loan) {
        return new LoanResponse(
            loan.getLoanId(),
            loan.getBookCopy() != null ? loan.getBookCopy().getCopyId() : null,
            loan.getStudent() != null ? loan.getStudent().getStudentId() : null,
            loan.getStaff() != null ? loan.getStaff().getStaffId() : null,
            loan.getLoanDate(),
            loan.getDueDate()
        );
    }
}
