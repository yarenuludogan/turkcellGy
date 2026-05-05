package com.libraryspring.turkcell.repository;

import com.libraryspring.turkcell.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    // Kategoriyi adına göre 
    List<Category> findByName(String name);
}
