package com.libraryspring.turkcell.repository;

import com.libraryspring.turkcell.entity.ReturnTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReturnTableRepository extends JpaRepository<ReturnTable, Long> {
    
    // Belirli bir loanın iadesi
    List<ReturnTable> findByLoan_LoanId(Long loanId);
    
    // Belirli bir tarihe kadar olan iade 
    List<ReturnTable> findByReturnDateBefore(LocalDate returnDate);
    
    // Belirli bir duruma göre iade 
    List<ReturnTable> findByConditionNotes(String conditionNotes);
}
