package com.libraryspring.turkcell.controller;

import com.libraryspring.turkcell.dto.request.LoanCreateRequest;
import com.libraryspring.turkcell.dto.response.LoanResponse;
import com.libraryspring.turkcell.entity.*;
import com.libraryspring.turkcell.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/loans")
public class LoanController {
    
    @Autowired
    private LoanService loanService;
    
    @Autowired
    private BookCopyService bookCopyService;
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private StaffService staffService;
    
    @PostMapping
    public ResponseEntity<LoanResponse> createLoan(@Valid @RequestBody LoanCreateRequest request) {
        BookCopy bookCopy = bookCopyService.findById(request.bookCopyId())
            .orElseThrow(() -> new RuntimeException("BookCopy not found"));
        Student student = studentService.findById(request.studentId())
            .orElseThrow(() -> new RuntimeException("Student not found"));
        Staff staff = null;
        if (request.staffId() != null) {
            staff = staffService.findById(request.staffId()).orElse(null);
        }
        
        Loan loan = new Loan(bookCopy, student, staff, request.loanDate(), request.dueDate());
        Loan saved = loanService.save(loan);
        return ResponseEntity.status(HttpStatus.CREATED).body(LoanResponse.fromEntity(saved));
    }
    
    @GetMapping
    public ResponseEntity<List<LoanResponse>> getAllLoans() {
        List<Loan> loans = loanService.findAll();
        List<LoanResponse> responses = loans.stream()
            .map(LoanResponse::fromEntity)
            .toList();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<LoanResponse> getLoanById(@PathVariable Long id) {
        return loanService.findById(id)
            .map(l -> ResponseEntity.ok(LoanResponse.fromEntity(l)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<LoanResponse>> getLoansByStudent(@PathVariable Long studentId) {
        List<Loan> loans = loanService.findByStudentId(studentId);
        List<LoanResponse> responses = loans.stream()
            .map(LoanResponse::fromEntity)
            .toList();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/overdue")
    public ResponseEntity<List<LoanResponse>> getOverdueLoans() {
        List<Loan> loans = loanService.findOverdueLoans();
        List<LoanResponse> responses = loans.stream()
            .map(LoanResponse::fromEntity)
            .toList();
        return ResponseEntity.ok(responses);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<LoanResponse> updateLoan(
            @PathVariable Long id,
            @Valid @RequestBody LoanCreateRequest request) {
        Loan updatedLoan = new Loan();
        updatedLoan.setDueDate(request.dueDate());
        Loan saved = loanService.update(id, updatedLoan);
        return ResponseEntity.ok(LoanResponse.fromEntity(saved));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        loanService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
