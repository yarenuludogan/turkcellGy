package com.libraryspring.turkcell.service;

import com.libraryspring.turkcell.entity.BookCopy;
import com.libraryspring.turkcell.repository.BookCopyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

//İş katmanı
@Service
public class BookCopyService {
    
    @Autowired
    private BookCopyRepository bookCopyRepository;
    
    @Transactional
    public BookCopy save(BookCopy bookCopy) {
        if (bookCopy.getBook() == null) {
            throw new RuntimeException("BookCopy must have a book reference");
        }
        return bookCopyRepository.save(bookCopy);
    }
    
    public List<BookCopy> findAll() {
        return bookCopyRepository.findAll();
    }
    
    public Optional<BookCopy> findById(Long id) {
        return bookCopyRepository.findById(id);
    }
    
    public List<BookCopy> findByBookId(Long bookId) {
        return bookCopyRepository.findByBook_BookId(bookId);
    }
    
    public List<BookCopy> findByStatus(String status) {
        return bookCopyRepository.findByStatus(status);
    }
    
    public List<BookCopy> findByShelfLocation(String shelfLocation) {
        return bookCopyRepository.findByShelfLocation(shelfLocation);
    }
    
    // Mevcut kopya sayısı
    public List<BookCopy> findAvailableCopies() {
        return bookCopyRepository.findByStatus("Available");
    }
    
    @Transactional
    public BookCopy update(Long id, BookCopy updatedBookCopy) {
        Optional<BookCopy> existing = findById(id);
        if (existing.isPresent()) {
            BookCopy bookCopy = existing.get();
            bookCopy.setStatus(updatedBookCopy.getStatus());
            bookCopy.setShelfLocation(updatedBookCopy.getShelfLocation());
            return save(bookCopy);
        }
        throw new RuntimeException("BookCopy with ID " + id + " not found");
    }
    
    @Transactional
    public void delete(Long id) {
        bookCopyRepository.deleteById(id);
    }
}
