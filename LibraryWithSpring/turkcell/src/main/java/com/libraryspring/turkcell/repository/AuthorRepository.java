package com.libraryspring.turkcell.repository;

import com.libraryspring.turkcell.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Author entity'si için veri erişim katmanı
 * JpaRepository, CRUD işlemlerini otomatik olarak sağlar:
 */
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findByName(String name);
    
    List<Author> findByCountry(String country);
}
