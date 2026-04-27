package com.turkcell.spring_starter.dto.request;

import java.util.UUID;

public class UpdateProductRequest {
    
    private UUID categoryId;
    private String name;
    private String description;
    public UUID getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    

    
}

