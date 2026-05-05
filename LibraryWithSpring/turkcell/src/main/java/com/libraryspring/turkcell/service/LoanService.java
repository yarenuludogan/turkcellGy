package com.libraryspring.turkcell.service;

import com.libraryspring.turkcell.entity.Loan;
import com.libraryspring.turkcell.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class LoanService {
    
    @Autowired
    private LoanRepository loanRepository;
    
    @Transactional
    public Loan save(Loan loan) {
  
        if (loan.getBookCopy() == null) {
            throw new RuntimeException("Loan must have a book copy");
        }
        if (loan.getStudent() == null) {
            throw new RuntimeException("Loan must have a student");
        }
        if (loan.getLoanDate() == null) {
            throw new RuntimeException("Loan must have a loan date");
        }
        if (loan.getDueDate() == null) {
            throw new RuntimeException("Loan must have a due date");
        }
        return loanRepository.save(loan);
    }
    
    public List<Loan> findAll() {
        return loanRepository.findAll();
    }
    
    public Optional<Loan> findById(Long id) {
        return loanRepository.findById(id);
    }
    
    public List<Loan> findByStudentId(Long studentId) {
        return loanRepository.findByStudent_StudentId(studentId);
    }
    
    public List<Loan> findByStaffId(Long staffId) {
        return loanRepository.findByStaff_StaffId(staffId);
    }
    
  
    public List<Loan> findOverdueLoans() {
        return loanRepository.findByDueDateBefore(LocalDate.now());
    }
    
    public List<Loan> findByBookCopyId(Long copyId) {
        return loanRepository.findByBookCopy_CopyId(copyId);
    }
    
    @Transactional
    public Loan update(Long id, Loan updatedLoan) {
        Optional<Loan> existing = findById(id);
        if (existing.isPresent()) {
            Loan loan = existing.get();
            if (updatedLoan.getDueDate() != null) {
                loan.setDueDate(updatedLoan.getDueDate());
            }
            return save(loan);
        }
        throw new RuntimeException("Loan with ID " + id + " not found");
    }
    
    @Transactional
    public void delete(Long id) {
        loanRepository.deleteById(id);
    }
}
