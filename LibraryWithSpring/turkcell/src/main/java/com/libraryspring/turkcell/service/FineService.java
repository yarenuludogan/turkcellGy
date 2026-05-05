package com.libraryspring.turkcell.service;

import com.libraryspring.turkcell.entity.Fine;
import com.libraryspring.turkcell.repository.FineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Service
public class FineService {
    
    @Autowired
    private FineRepository fineRepository;
    
    @Transactional
    public Fine save(Fine fine) {
        if (fine.getLoan() == null) {
            throw new RuntimeException("Fine must have a loan reference");
        }
        if (fine.getAmount() == null || fine.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Fine amount must be valid");
        }
        return fineRepository.save(fine);
    }
    
    public List<Fine> findAll() {
        return fineRepository.findAll();
    }
    
    public Optional<Fine> findById(Long id) {
        return fineRepository.findById(id);
    }
    
    public List<Fine> findByLoanId(Long loanId) {
        return fineRepository.findByLoan_LoanId(loanId);
    }
    
    public List<Fine> findUnpaidFines() {
        return fineRepository.findByPaidFalse();
    }
    

    public List<Fine> findPaidFines() {
        return fineRepository.findByPaidTrue();
    }
    
    public List<Fine> findByAmountGreaterThan(BigDecimal amount) {
        return fineRepository.findByAmountGreaterThan(amount);
    }
    
    // Ceza ödemesini işle
    @Transactional
    public Fine markAsPaid(Long fineId) {
        Optional<Fine> fine = findById(fineId);
        if (fine.isPresent()) {
            Fine f = fine.get();
            f.setPaid(true);
            return save(f);
        }
        throw new RuntimeException("Fine with ID " + fineId + " not found");
    }
    
    @Transactional
    public Fine update(Long id, Fine updatedFine) {
        Optional<Fine> existing = findById(id);
        if (existing.isPresent()) {
            Fine fine = existing.get();
            fine.setAmount(updatedFine.getAmount());
            fine.setPaid(updatedFine.getPaid());
            return save(fine);
        }
        throw new RuntimeException("Fine with ID " + id + " not found");
    }
    
    @Transactional
    public void delete(Long id) {
        fineRepository.deleteById(id);
    }
}
