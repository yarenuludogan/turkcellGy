package com.libraryspring.turkcell.service;

import com.libraryspring.turkcell.entity.Book;
import com.libraryspring.turkcell.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;


@Service
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;
    
    @Transactional
    public Book save(Book book) {
     
        if (book.getAuthor() == null) {
            throw new RuntimeException("Book must have an author");
        }
        return bookRepository.save(book);
    }
    
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
    
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }
    
    public List<Book> findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }
    
    public Book findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }
    
    public List<Book> findByAuthorId(Long authorId) {
        return bookRepository.findByAuthor_AuthorId(authorId);
    }
    
    public List<Book> findByPublisherId(Long publisherId) {
        return bookRepository.findByPublisher_PublisherId(publisherId);
    }
    
    public List<Book> findByCategoryId(Long categoryId) {
        return bookRepository.findByCategory_CategoryId(categoryId);
    }
    
    public List<Book> findByPublishedYear(Integer publishedYear) {
        return bookRepository.findByPublishedYear(publishedYear);
    }
    
    @Transactional
    public Book update(Long id, Book updatedBook) {
        Optional<Book> existing = findById(id);
        if (existing.isPresent()) {
            Book book = existing.get();
            book.setTitle(updatedBook.getTitle());
            book.setIsbn(updatedBook.getIsbn());
            book.setPublishedYear(updatedBook.getPublishedYear());
            book.setPageCount(updatedBook.getPageCount());
            if (updatedBook.getAuthor() != null) {
                book.setAuthor(updatedBook.getAuthor());
            }
            if (updatedBook.getPublisher() != null) {
                book.setPublisher(updatedBook.getPublisher());
            }
            if (updatedBook.getCategory() != null) {
                book.setCategory(updatedBook.getCategory());
            }
            return save(book);
        }
        throw new RuntimeException("Book with ID " + id + " not found");
    }
    
    @Transactional
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}
