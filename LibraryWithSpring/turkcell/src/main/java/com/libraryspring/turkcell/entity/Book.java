package com.libraryspring.turkcell.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

/**
 * Book Entity - Library.sql'deki Book tablosunu temsil eder
 * Bir kitap:
 * - Bir yazara ait (Many-to-One ile Author)
 * - Bir yayıncıya ait (Many-to-One ile Publisher)
 * - Bir kategoriye ait (Many-to-One ile Category)
 * - Birden fazla kopyaya sahip (One-to-Many ile BookCopy)
 */
@Entity
@Table(name = "book")
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "isbn", unique = true, length = 20)
    private String isbn;

    @Column(name = "publish_year")
    private Integer publishedYear;

    @Column(name = "page_count")
    private Integer pageCount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BookCopy> copies;

    public Book() {}
    public Book(String title, String isbn, Integer publishedYear, Integer pageCount, 
                Author author, Publisher publisher, Category category) {
        this.title = title;
        this.isbn = isbn;
        this.publishedYear = publishedYear;
        this.pageCount = pageCount;
        this.author = author;
        this.publisher = publisher;
        this.category = category;
    }

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public Integer getPublishedYear() { return publishedYear; }
    public void setPublishedYear(Integer publishedYear) { this.publishedYear = publishedYear; }
    public Integer getPageCount() { return pageCount; }
    public void setPageCount(Integer pageCount) { this.pageCount = pageCount; }
    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }
    public Publisher getPublisher() { return publisher; }
    public void setPublisher(Publisher publisher) { this.publisher = publisher; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public List<BookCopy> getCopies() { return copies; }
    public void setCopies(List<BookCopy> copies) { this.copies = copies; }
}
