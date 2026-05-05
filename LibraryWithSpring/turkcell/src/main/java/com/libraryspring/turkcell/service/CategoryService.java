package com.libraryspring.turkcell.service;

import com.libraryspring.turkcell.entity.Category;
import com.libraryspring.turkcell.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Transactional
    public Category save(Category category) {
        return categoryRepository.save(category);
    }
    
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
    
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }
    
    public List<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }
    
    @Transactional
    public Category update(Long id, Category updatedCategory) {
        Optional<Category> existing = findById(id);
        if (existing.isPresent()) {
            Category category = existing.get();
            category.setName(updatedCategory.getName());
            return save(category);
        }
        throw new RuntimeException("Category with ID " + id + " not found");
    }
    
    @Transactional
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
