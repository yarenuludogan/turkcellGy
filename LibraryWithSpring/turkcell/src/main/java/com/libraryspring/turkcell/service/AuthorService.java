package com.libraryspring.turkcell.service;

import com.libraryspring.turkcell.entity.Author;
import com.libraryspring.turkcell.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * AuthorService - Author entity'si için iş mantığı katmanı
 * CRUD işlemleri: Create, Read, Update, Delete
 * @Transactional ile transaction yönetimi
 */
@Service
public class AuthorService {
    
    @Autowired
    private AuthorRepository authorRepository;
    
    // CREATE 
    @Transactional
    public Author save(Author author) {
        return authorRepository.save(author);
    }
    
    // READ 
    public List<Author> findAll() {
        return authorRepository.findAll();
    }
    
    // READ - ID
    public Optional<Author> findById(Long id) {
        return authorRepository.findById(id);
    }
    
    // READ - Ad
    public List<Author> findByName(String name) {
        return authorRepository.findByName(name);
    }
    
    // READ - Ülke
    public List<Author> findByCountry(String country) {
        return authorRepository.findByCountry(country);
    }
    
    // UPDATE 
    @Transactional
    public Author update(Long id, Author updatedAuthor) {
        Optional<Author> existing = findById(id);
        if (existing.isPresent()) {
            Author author = existing.get();
            author.setName(updatedAuthor.getName());
            author.setCountry(updatedAuthor.getCountry());
            return save(author);
        }
        throw new RuntimeException("Author with ID " + id + " not found");
    }
    
    // DELETE 
    @Transactional
    public void delete(Long id) {
        authorRepository.deleteById(id);
    }
}
