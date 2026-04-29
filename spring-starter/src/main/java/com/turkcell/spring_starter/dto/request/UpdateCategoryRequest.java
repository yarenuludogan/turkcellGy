package com.turkcell.spring_starter.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateCategoryRequest {

    @NotBlank(message = "Category name cannot be blank")
    @Size(min = 1, max = 100, message = "Category name must be between 1 and 100 characters")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

