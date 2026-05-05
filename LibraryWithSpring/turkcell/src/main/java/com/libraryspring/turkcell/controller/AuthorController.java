package com.libraryspring.turkcell.controller;

import com.libraryspring.turkcell.dto.request.AuthorCreateRequest;
import com.libraryspring.turkcell.dto.response.AuthorResponse;
import com.libraryspring.turkcell.entity.Author;
import com.libraryspring.turkcell.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 *
 * Base URL: /api/authors
 * 
 * - POST /api/authors => Yazar oluştur
 * - GET /api/authors => Tüm yazarları listele
 * - GET /api/authors/{id} => ID'ye göre yazar getir
 * - PUT /api/authors/{id} => Yazarı güncelle
 * - DELETE /api/authors/{id} => Yazarı sil
 */
@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    
    @Autowired
    private AuthorService authorService;
    
    //Yeni yazar
    @PostMapping
    public ResponseEntity<AuthorResponse> createAuthor(@Valid @RequestBody AuthorCreateRequest request) {
        Author author = new Author(request.name(), request.country());
        Author saved = authorService.save(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(AuthorResponse.fromEntity(saved));
    }
    
    //Tüm yazarlar
    @GetMapping
    public ResponseEntity<List<AuthorResponse>> getAllAuthors() {
        List<Author> authors = authorService.findAll();
        List<AuthorResponse> responses = authors.stream()
            .map(AuthorResponse::fromEntity)
            .toList();
        return ResponseEntity.ok(responses);
    }
    
    //ID'ye göre yazar
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> getAuthorById(@PathVariable Long id) {
        return authorService.findById(id)
            .map(author -> ResponseEntity.ok(AuthorResponse.fromEntity(author)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    //Adına göre yazarlar
    @GetMapping("/search/name")
    public ResponseEntity<List<AuthorResponse>> getAuthorsByName(@RequestParam String name) {
        List<Author> authors = authorService.findByName(name);
        List<AuthorResponse> responses = authors.stream()
            .map(AuthorResponse::fromEntity)
            .toList();
        return ResponseEntity.ok(responses);
    }
    
    //Ülkeye göre yazarlar
    @GetMapping("/search/country")
    public ResponseEntity<List<AuthorResponse>> getAuthorsByCountry(@RequestParam String country) {
        List<Author> authors = authorService.findByCountry(country);
        List<AuthorResponse> responses = authors.stream()
            .map(AuthorResponse::fromEntity)
            .toList();
        return ResponseEntity.ok(responses);
    }
    
    //Yazarı güncelle
    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponse> updateAuthor(
            @PathVariable Long id,
            @Valid @RequestBody AuthorCreateRequest request) {
        Author updatedAuthor = new Author(request.name(), request.country());
        Author saved = authorService.update(id, updatedAuthor);
        return ResponseEntity.ok(AuthorResponse.fromEntity(saved));
    }
    
    //Yazarı sil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
