package com.turkcell.spring_starter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.spring_starter.dto.request.CreateCategoryRequest;
import com.turkcell.spring_starter.dto.response.CreatedCategoryResponse;
import com.turkcell.spring_starter.dto.response.GetCategoryResponse;
import com.turkcell.spring_starter.dto.response.ListCategoryResponse;
import com.turkcell.spring_starter.dto.request.UpdateCategoryRequest;
import com.turkcell.spring_starter.dto.response.UpdatedCategoryResponse;
import com.turkcell.spring_starter.service.CategoryServiceImpl;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {
    private final CategoryServiceImpl categoryServiceImpl;

    public CategoriesController(CategoryServiceImpl categoryServiceImpl) {
        this.categoryServiceImpl = categoryServiceImpl;
    }

    @GetMapping
    public List<ListCategoryResponse> getAll() {
        return categoryServiceImpl.getAll();
    }

    @GetMapping("/{id}")
    public GetCategoryResponse getById(@PathVariable UUID id) {
        return categoryServiceImpl.getById(id);
    }
    
    @PutMapping("/{id}")
    public UpdatedCategoryResponse update(@PathVariable UUID id, @RequestBody @Valid UpdateCategoryRequest updateCategoryRequest) {
        return categoryServiceImpl.update(id, updateCategoryRequest);
    }

    @PostMapping()
        public CreatedCategoryResponse create(@RequestBody @Valid CreateCategoryRequest createCategoryRequest)
        {
        return categoryServiceImpl.create(createCategoryRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        categoryServiceImpl.delete(id);
    }
}

