package com.libraryspring.turkcell.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

/**
 * Bir kitabın fiziksel kopyasını temsil eder
 * Bir kitap (Book) birden fazla kopyaya sahip olabilir (Many-to-One)
 * Bir kopya birden fazla ödünçlemeye (Loan) konu olabilir (One-to-Many)
 */
@Entity
@Table(name = "book_copy")
public class BookCopy {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "copy_id")
    private Long copyId;

    /**
     * Many-to-One ilişkisi: Bir kopya bir kitaya ait
     * @JoinColumn(name = "book_id") => Library.sql'deki foreign key
     * nullable = false => Her kopyanın bir kitabı olması zorunlu
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "status", length = 30)
    private String status;

  
    @Column(name = "shelf_location", length = 50)
    private String shelfLocation;

    /**
     * One-to-Many ilişkisi: Bir kopya birden fazla ödünçlemeye konu olabilir
     * mappedBy = "bookCopy" => Loan entity'sinde "bookCopy" alanı bu ilişkiyi tanımlar
     *
     * @JsonIgnore 
     */
    @OneToMany(mappedBy = "bookCopy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Loan> loans;

    public BookCopy() {}

    public BookCopy(Book book, String status, String shelfLocation) {
        this.book = book;
        this.status = status;
        this.shelfLocation = shelfLocation;
    }

    public Long getCopyId() {
        return copyId;
    }

    public void setCopyId(Long copyId) {
        this.copyId = copyId;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShelfLocation() {
        return shelfLocation;
    }

    public void setShelfLocation(String shelfLocation) {
        this.shelfLocation = shelfLocation;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }
}
