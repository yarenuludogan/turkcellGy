package com.libraryspring.turkcell.controller;

import com.libraryspring.turkcell.dto.request.PublisherCreateRequest;
import com.libraryspring.turkcell.dto.response.PublisherResponse;
import com.libraryspring.turkcell.entity.Publisher;
import com.libraryspring.turkcell.service.PublisherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/publishers")
public class PublisherController {
    
    @Autowired
    private PublisherService publisherService;
    
    @PostMapping
    public ResponseEntity<PublisherResponse> createPublisher(@Valid @RequestBody PublisherCreateRequest request) {
        Publisher publisher = new Publisher(request.name(), request.address());
        Publisher saved = publisherService.save(publisher);
        return ResponseEntity.status(HttpStatus.CREATED).body(PublisherResponse.fromEntity(saved));
    }
    
    @GetMapping
    public ResponseEntity<List<PublisherResponse>> getAllPublishers() {
        List<Publisher> publishers = publisherService.findAll();
        List<PublisherResponse> responses = publishers.stream()
            .map(PublisherResponse::fromEntity)
            .toList();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PublisherResponse> getPublisherById(@PathVariable Long id) {
        return publisherService.findById(id)
            .map(p -> ResponseEntity.ok(PublisherResponse.fromEntity(p)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PublisherResponse> updatePublisher(
            @PathVariable Long id,
            @Valid @RequestBody PublisherCreateRequest request) {
        Publisher updatedPublisher = new Publisher(request.name(), request.address());
        Publisher saved = publisherService.update(id, updatedPublisher);
        return ResponseEntity.ok(PublisherResponse.fromEntity(saved));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable Long id) {
        publisherService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
