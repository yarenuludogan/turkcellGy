package com.libraryspring.turkcell.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Bir iade işlemi bir loanaait Many-to-One
 * 
 * Tablo adı "return_table" olarak kullanılmalı çünkü "return" SQL de ayrı bir kelime
 */
@Entity
@Table(name = "return_table")
public class ReturnTable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "return_id")
    private Long returnId;

    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

   
    @Column(name = "return_date", nullable = false)
    private LocalDate returnDate;

    @Column(name = "condition_notes", length = 255)
    private String conditionNotes;

  
    public ReturnTable() {}

    public ReturnTable(Loan loan, LocalDate returnDate, String conditionNotes) {
        this.loan = loan;
        this.returnDate = returnDate;
        this.conditionNotes = conditionNotes;
    }

    public Long getReturnId() {
        return returnId;
    }

    public void setReturnId(Long returnId) {
        this.returnId = returnId;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getConditionNotes() {
        return conditionNotes;
    }

    public void setConditionNotes(String conditionNotes) {
        this.conditionNotes = conditionNotes;
    }
}
