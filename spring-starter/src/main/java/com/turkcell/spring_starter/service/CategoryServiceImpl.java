package com.turkcell.spring_starter.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.turkcell.spring_starter.dto.request.CreateCategoryRequest;
import com.turkcell.spring_starter.dto.response.CreatedCategoryResponse;
import com.turkcell.spring_starter.dto.response.ListCategoryResponse;
import com.turkcell.spring_starter.dto.response.GetCategoryResponse;
import com.turkcell.spring_starter.dto.request.UpdateCategoryRequest;
import com.turkcell.spring_starter.dto.response.UpdatedCategoryResponse;
import com.turkcell.spring_starter.entity.Category;
import com.turkcell.spring_starter.repository.CategoryRepository;

@Service
public class CategoryServiceImpl {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CreatedCategoryResponse create(CreateCategoryRequest createCategoryRequest) {
 
        Category category = new Category();
        category.setName(createCategoryRequest.getName());
        category = this.categoryRepository.save(category); 

        CreatedCategoryResponse response = new CreatedCategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());

        return response;
    } 

    public List<ListCategoryResponse> getAll() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(category -> {
                    ListCategoryResponse response = new ListCategoryResponse();
                    response.setId(category.getId());
                    response.setName(category.getName());
                    return response;
                })
                .collect(Collectors.toList());
    }

    public GetCategoryResponse getById(UUID id) {

        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));

        GetCategoryResponse response = new GetCategoryResponse();

        response.setId(category.getId());
        response.setName(category.getName());
        return response;

    }

    public UpdatedCategoryResponse update(UUID id, UpdateCategoryRequest updateCategoryRequest) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(updateCategoryRequest.getName());
        category = categoryRepository.save(category);

        UpdatedCategoryResponse response = new UpdatedCategoryResponse();

        response.setId(category.getId());
        response.setName(category.getName());

        return response;
    }

    public void delete(UUID id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepository.delete(category);
    }
}

