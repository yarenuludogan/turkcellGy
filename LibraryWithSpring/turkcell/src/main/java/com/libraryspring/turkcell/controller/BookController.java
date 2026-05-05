package com.libraryspring.turkcell.controller;

import com.libraryspring.turkcell.dto.request.BookCreateRequest;
import com.libraryspring.turkcell.dto.response.BookResponse;
import com.libraryspring.turkcell.entity.Book;
import com.libraryspring.turkcell.entity.Author;
import com.libraryspring.turkcell.entity.Publisher;
import com.libraryspring.turkcell.entity.Category;
import com.libraryspring.turkcell.service.BookService;
import com.libraryspring.turkcell.service.AuthorService;
import com.libraryspring.turkcell.service.PublisherService;
import com.libraryspring.turkcell.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 
 * Base URL: /api/books
 * 
 * Kitap oluştururken zorunlu: title, publishedYear, authorId
 * Author (Many-to-One), Publisher (Many-to-One), Category (Many-to-One)
 */
@RestController
@RequestMapping("/api/books")
public class BookController {
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private AuthorService authorService;
    
    @Autowired
    private PublisherService publisherService;
    
    @Autowired
    private CategoryService categoryService;
    
    // Yeni kitap
    @PostMapping
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookCreateRequest request) {
       
        Author author = authorService.findById(request.authorId())
            .orElseThrow(() -> new RuntimeException("Author not found with ID: " + request.authorId()));
        
        Publisher publisher = null;
        if (request.publisherId() != null) {
            publisher = publisherService.findById(request.publisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found with ID: " + request.publisherId()));
        }
        
        Category category = null;
        if (request.categoryId() != null) {
            category = categoryService.findById(request.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + request.categoryId()));
        }
        
        Book book = new Book(request.title(), request.isbn(), request.publishedYear(), request.pageCount(),
                            author, publisher, category);
        Book saved = bookService.save(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(BookResponse.fromEntity(saved));
    }
    
    //Tüm kitaplar
    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        List<Book> books = bookService.findAll();
        List<BookResponse> responses = books.stream()
            .map(BookResponse::fromEntity)
            .toList();
        return ResponseEntity.ok(responses);
    }
    
    //ID'ye göre kitap
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        return bookService.findById(id)
            .map(book -> ResponseEntity.ok(BookResponse.fromEntity(book)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    //Başlığa göre kitap
    @GetMapping("/search/title")
    public ResponseEntity<List<BookResponse>> getBooksByTitle(@RequestParam String title) {
        List<Book> books = bookService.findByTitle(title);
        List<BookResponse> responses = books.stream()
            .map(BookResponse::fromEntity)
            .toList();
        return ResponseEntity.ok(responses);
    }
    
    //ISBN göre kitap
    @GetMapping("/search/isbn/{isbn}")
    public ResponseEntity<BookResponse> getBookByIsbn(@PathVariable String isbn) {
        Book book = bookService.findByIsbn(isbn);
        return book != null
            ? ResponseEntity.ok(BookResponse.fromEntity(book))
            : ResponseEntity.notFound().build();
    }
    
    //Yazara göre kitaplar
    @GetMapping("/search/author/{authorId}")
    public ResponseEntity<List<BookResponse>> getBooksByAuthor(@PathVariable Long authorId) {
        List<Book> books = bookService.findByAuthorId(authorId);
        List<BookResponse> responses = books.stream()
            .map(BookResponse::fromEntity)
            .toList();
        return ResponseEntity.ok(responses);
    }
    
    //Kitabı güncelle
    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookCreateRequest request) {
        Author author = authorService.findById(request.authorId())
            .orElseThrow(() -> new RuntimeException("Author not found with ID: " + request.authorId()));
        
        Publisher publisher = null;
        if (request.publisherId() != null) {
            publisher = publisherService.findById(request.publisherId()).orElse(null);
        }
        
        Category category = null;
        if (request.categoryId() != null) {
            category = categoryService.findById(request.categoryId()).orElse(null);
        }
        
        Book updatedBook = new Book(request.title(), request.isbn(), request.publishedYear(), request.pageCount(),
                                    author, publisher, category);
        Book saved = bookService.update(id, updatedBook);
        return ResponseEntity.ok(BookResponse.fromEntity(saved));
    }
    
    //Kitabı sil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
