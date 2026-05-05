package com.libraryspring.turkcell.controller;

import com.libraryspring.turkcell.dto.request.ReturnTableCreateRequest;
import com.libraryspring.turkcell.dto.response.ReturnTableResponse;
import com.libraryspring.turkcell.entity.ReturnTable;
import com.libraryspring.turkcell.entity.Loan;
import com.libraryspring.turkcell.service.ReturnTableService;
import com.libraryspring.turkcell.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/returns")
public class ReturnTableController {
    
    @Autowired
    private ReturnTableService returnTableService;
    
    @Autowired
    private LoanService loanService;
    
    @PostMapping
    public ResponseEntity<ReturnTableResponse> createReturn(@Valid @RequestBody ReturnTableCreateRequest request) {
        Loan loan = loanService.findById(request.loanId())
            .orElseThrow(() -> new RuntimeException("Loan not found"));
        
        ReturnTable returnTable = new ReturnTable(loan, request.returnDate(), request.conditionNotes());
        ReturnTable saved = returnTableService.save(returnTable);
        return ResponseEntity.status(HttpStatus.CREATED).body(ReturnTableResponse.fromEntity(saved));
    }
    
    @GetMapping
    public ResponseEntity<List<ReturnTableResponse>> getAllReturns() {
        List<ReturnTable> returns = returnTableService.findAll();
        List<ReturnTableResponse> responses = returns.stream()
            .map(ReturnTableResponse::fromEntity)
            .toList();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ReturnTableResponse> getReturnById(@PathVariable Long id) {
        return returnTableService.findById(id)
            .map(r -> ResponseEntity.ok(ReturnTableResponse.fromEntity(r)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/loan/{loanId}")
    public ResponseEntity<List<ReturnTableResponse>> getReturnsByLoan(@PathVariable Long loanId) {
        List<ReturnTable> returns = returnTableService.findByLoanId(loanId);
        List<ReturnTableResponse> responses = returns.stream()
            .map(ReturnTableResponse::fromEntity)
            .toList();
        return ResponseEntity.ok(responses);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReturn(@PathVariable Long id) {
        returnTableService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
