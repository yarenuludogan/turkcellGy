package com.turkcell.spring_starter.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.spring_starter.dto.request.CreateProductRequest;
import com.turkcell.spring_starter.dto.response.CreatedProductResponse;
import com.turkcell.spring_starter.dto.response.GetProductResponse;
import com.turkcell.spring_starter.dto.response.ListProductResponse;
import com.turkcell.spring_starter.dto.request.UpdateProductRequest;
import com.turkcell.spring_starter.dto.response.UpdatedProductResponse;
import com.turkcell.spring_starter.service.ProductServiceImpl;


@RestController
@RequestMapping("/api/products")
public class ProductsController {
    private final ProductServiceImpl productServiceImpl;

    public ProductsController(ProductServiceImpl productServiceImpl) {
        this.productServiceImpl = productServiceImpl;
    }

    @PutMapping("/{id}")
        public UpdatedProductResponse update(@PathVariable UUID id, @RequestBody UpdateProductRequest updateProductRequest) {
            return productServiceImpl.update(id, updateProductRequest);
    }

    @GetMapping
    public List<ListProductResponse> getAll() {
        return productServiceImpl.getAll();
    }

    @GetMapping("/{id}")
    public GetProductResponse getById(@PathVariable UUID id) {
        return productServiceImpl.getById(id);
    }

    @PostMapping
    public CreatedProductResponse create(@RequestBody CreateProductRequest createProductRequest) {
        return productServiceImpl.create(createProductRequest);
    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        productServiceImpl.delete(id);
    }
}

