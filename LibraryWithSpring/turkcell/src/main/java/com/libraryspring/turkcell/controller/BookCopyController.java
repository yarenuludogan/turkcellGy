package com.libraryspring.turkcell.controller;

import com.libraryspring.turkcell.dto.request.BookCopyCreateRequest;
import com.libraryspring.turkcell.dto.response.BookCopyResponse;
import com.libraryspring.turkcell.entity.BookCopy;
import com.libraryspring.turkcell.entity.Book;
import com.libraryspring.turkcell.service.BookCopyService;
import com.libraryspring.turkcell.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/book-copies")
public class BookCopyController {
    
    @Autowired
    private BookCopyService bookCopyService;
    
    @Autowired
    private BookService bookService;
    
    @PostMapping
    public ResponseEntity<BookCopyResponse> createBookCopy(@Valid @RequestBody BookCopyCreateRequest request) {
        Book book = bookService.findById(request.bookId())
            .orElseThrow(() -> new RuntimeException("Book not found with ID: " + request.bookId()));
        
        BookCopy copy = new BookCopy(book, request.status(), request.shelfLocation());
        BookCopy saved = bookCopyService.save(copy);
        return ResponseEntity.status(HttpStatus.CREATED).body(BookCopyResponse.fromEntity(saved));
    }
    
    @GetMapping
    public ResponseEntity<List<BookCopyResponse>> getAllBookCopies() {
        List<BookCopy> copies = bookCopyService.findAll();
        List<BookCopyResponse> responses = copies.stream()
            .map(BookCopyResponse::fromEntity)
            .toList();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BookCopyResponse> getBookCopyById(@PathVariable Long id) {
        return bookCopyService.findById(id)
            .map(c -> ResponseEntity.ok(BookCopyResponse.fromEntity(c)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<BookCopyResponse>> getBookCopiesByBook(@PathVariable Long bookId) {
        List<BookCopy> copies = bookCopyService.findByBookId(bookId);
        List<BookCopyResponse> responses = copies.stream()
            .map(BookCopyResponse::fromEntity)
            .toList();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<BookCopyResponse>> getBookCopiesByStatus(@PathVariable String status) {
        List<BookCopy> copies = bookCopyService.findByStatus(status);
        List<BookCopyResponse> responses = copies.stream()
            .map(BookCopyResponse::fromEntity)
            .toList();
        return ResponseEntity.ok(responses);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<BookCopyResponse> updateBookCopy(
            @PathVariable Long id,
            @Valid @RequestBody BookCopyCreateRequest request) {
        BookCopy updatedCopy = new BookCopy(null, request.status(), request.shelfLocation());
        BookCopy saved = bookCopyService.update(id, updatedCopy);
        return ResponseEntity.ok(BookCopyResponse.fromEntity(saved));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookCopy(@PathVariable Long id) {
        bookCopyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
