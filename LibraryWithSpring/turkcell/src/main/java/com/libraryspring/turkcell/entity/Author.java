package com.libraryspring.turkcell.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

/**
 * 
 * Bir yazar birden fazla kitaba (Book) sahip olabilir One-to-Many 
 */
@Entity
@Table(name = "author")
public class Author {
    
    // Primary Key - Library.sql'de Author_Id (IDENTITY ile auto-increment)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private Long authorId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "country", length = 50)
    private String country;

    /**
     * One-to-Many ilişkisi: Bir yazar birden fazla kitaba sahip
     * mappedBy = "author" => Book entity'sinde "author" alanı bu ilişkiyi tanımlar
     * cascade = CascadeType.ALL => Yazar silinirse kitapları da silinir
     * fetch = FetchType.LAZY => Performans için (kitapları sadece istenince yükle)
     * @JsonIgnore => JSON'da kitapları gösterme (sonsuz döngü önlemek için)
     */
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Book> books;

  
    public Author() {}

    public Author(String name, String country) {
        this.name = name;
        this.country = country;
    }


    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}