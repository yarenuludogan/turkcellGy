package com.libraryspring.turkcell.controller;

import com.libraryspring.turkcell.dto.request.CategoryCreateRequest;
import com.libraryspring.turkcell.dto.response.CategoryResponse;
import com.libraryspring.turkcell.entity.Category;
import com.libraryspring.turkcell.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryCreateRequest request) {
        Category category = new Category(request.name());
        Category saved = categoryService.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(CategoryResponse.fromEntity(saved));
    }
    
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<Category> categories = categoryService.findAll();
        List<CategoryResponse> responses = categories.stream()
            .map(CategoryResponse::fromEntity)
            .toList();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        return categoryService.findById(id)
            .map(c -> ResponseEntity.ok(CategoryResponse.fromEntity(c)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryCreateRequest request) {
        Category updatedCategory = new Category(request.name());
        Category saved = categoryService.update(id, updatedCategory);
        return ResponseEntity.ok(CategoryResponse.fromEntity(saved));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
