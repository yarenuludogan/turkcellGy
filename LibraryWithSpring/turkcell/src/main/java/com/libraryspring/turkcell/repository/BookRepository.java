package com.libraryspring.turkcell.repository;

import com.libraryspring.turkcell.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * filtreleme için ekler var
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    // Kitabı adına göre 
    List<Book> findByTitle(String title);
    
    // Kitabı ISBN'ye göre 
    Book findByIsbn(String isbn);
    
    // Kitabı yazara göre 
    List<Book> findByAuthor_AuthorId(Long authorId);
    
    // Kitabı yayıncıya göre 
    List<Book> findByPublisher_PublisherId(Long publisherId);
    
    // Kitabı kategoriye göre 
    List<Book> findByCategory_CategoryId(Long categoryId);
    
    // Yayın yılına göre 
    List<Book> findByPublishedYear(Integer publishedYear);
}
