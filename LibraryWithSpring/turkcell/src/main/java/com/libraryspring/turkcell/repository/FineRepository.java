package com.libraryspring.turkcell.repository;

import com.libraryspring.turkcell.entity.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;


@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {
    
    // bir loanın cezası
    List<Fine> findByLoan_LoanId(Long loanId);
    
    // Ödenmemiş ceza
    List<Fine> findByPaidFalse();
    
    // Ödenen cezalar
    List<Fine> findByPaidTrue();
    
    // Belirli bir tutardan fazla olan ceza
    List<Fine> findByAmountGreaterThan(BigDecimal amount);
}
