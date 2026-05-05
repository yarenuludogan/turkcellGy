package com.libraryspring.turkcell.service;

import com.libraryspring.turkcell.entity.ReturnTable;
import com.libraryspring.turkcell.repository.ReturnTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class ReturnTableService {
    
    @Autowired
    private ReturnTableRepository returnTableRepository;
    
    @Transactional
    public ReturnTable save(ReturnTable returnTable) {
        if (returnTable.getLoan() == null) {
            throw new RuntimeException("Return must have a loan reference");
        }
        if (returnTable.getReturnDate() == null) {
            throw new RuntimeException("Return must have a return date");
        }
        return returnTableRepository.save(returnTable);
    }
    
    public List<ReturnTable> findAll() {
        return returnTableRepository.findAll();
    }
    
    public Optional<ReturnTable> findById(Long id) {
        return returnTableRepository.findById(id);
    }
    
    public List<ReturnTable> findByLoanId(Long loanId) {
        return returnTableRepository.findByLoan_LoanId(loanId);
    }
    
    public List<ReturnTable> findByReturnDateBefore(LocalDate returnDate) {
        return returnTableRepository.findByReturnDateBefore(returnDate);
    }
    
    public List<ReturnTable> findByConditionNotes(String conditionNotes) {
        return returnTableRepository.findByConditionNotes(conditionNotes);
    }
    
    // Hasarlı kitapları bul
    public List<ReturnTable> findDamagedReturns() {
        return returnTableRepository.findByConditionNotes("Damaged");
    }
    
    @Transactional
    public ReturnTable update(Long id, ReturnTable updatedReturnTable) {
        Optional<ReturnTable> existing = findById(id);
        if (existing.isPresent()) {
            ReturnTable returnTable = existing.get();
            returnTable.setConditionNotes(updatedReturnTable.getConditionNotes());
            return save(returnTable);
        }
        throw new RuntimeException("Return with ID " + id + " not found");
    }
    
    @Transactional
    public void delete(Long id) {
        returnTableRepository.deleteById(id);
    }
}
