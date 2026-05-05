package com.libraryspring.turkcell.controller;

import com.libraryspring.turkcell.dto.request.FineCreateRequest;
import com.libraryspring.turkcell.dto.response.FineResponse;
import com.libraryspring.turkcell.entity.Fine;
import com.libraryspring.turkcell.entity.Loan;
import com.libraryspring.turkcell.service.FineService;
import com.libraryspring.turkcell.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/fines")
public class FineController {
    
    @Autowired
    private FineService fineService;
    
    @Autowired
    private LoanService loanService;
    
    @PostMapping
    public ResponseEntity<FineResponse> createFine(@Valid @RequestBody FineCreateRequest request) {
        Loan loan = loanService.findById(request.loanId())
            .orElseThrow(() -> new RuntimeException("Loan not found"));
        
        Fine fine = new Fine(loan, request.amount());
        if (request.paid() != null) {
            fine.setPaid(request.paid());
        }
        Fine saved = fineService.save(fine);
        return ResponseEntity.status(HttpStatus.CREATED).body(FineResponse.fromEntity(saved));
    }
    
    @GetMapping
    public ResponseEntity<List<FineResponse>> getAllFines() {
        List<Fine> fines = fineService.findAll();
        List<FineResponse> responses = fines.stream()
            .map(FineResponse::fromEntity)
            .toList();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<FineResponse> getFineById(@PathVariable Long id) {
        return fineService.findById(id)
            .map(f -> ResponseEntity.ok(FineResponse.fromEntity(f)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/loan/{loanId}")
    public ResponseEntity<List<FineResponse>> getFinesByLoan(@PathVariable Long loanId) {
        List<Fine> fines = fineService.findByLoanId(loanId);
        List<FineResponse> responses = fines.stream()
            .map(FineResponse::fromEntity)
            .toList();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/unpaid")
    public ResponseEntity<List<FineResponse>> getUnpaidFines() {
        List<Fine> fines = fineService.findUnpaidFines();
        List<FineResponse> responses = fines.stream()
            .map(FineResponse::fromEntity)
            .toList();
        return ResponseEntity.ok(responses);
    }
    
    @PutMapping("/{id}/pay")
    public ResponseEntity<FineResponse> markAsPaid(@PathVariable Long id) {
        Fine fine = fineService.markAsPaid(id);
        return ResponseEntity.ok(FineResponse.fromEntity(fine));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFine(@PathVariable Long id) {
        fineService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
