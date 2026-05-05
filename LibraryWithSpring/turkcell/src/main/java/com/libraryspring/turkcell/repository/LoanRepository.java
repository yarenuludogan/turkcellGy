package com.libraryspring.turkcell.repository;

import com.libraryspring.turkcell.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

/**
 * LoanRepository - Loan entity'si için veri erişim katmanı
 * Ödünçleme işlemlerini yönetir
 */
@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    
    // Belirli bir öğrencinin loan
    List<Loan> findByStudent_StudentId(Long studentId);
    
    // Belirli bir personelin işlediği loan
    List<Loan> findByStaff_StaffId(Long staffId);
    
    // Belirli bir tarihe kadar geçerli olan loan
    List<Loan> findByDueDateBefore(LocalDate dueDate);
    
    // Belirli bir kitap kopyasının loan
    List<Loan> findByBookCopy_CopyId(Long copyId);
}
