package com.libraryspring.turkcell.dto.response;

import com.libraryspring.turkcell.entity.Category;

/**
 * CategoryResponse - Kategori bilgilerini döndürmek için response DTO'su
 */
public record CategoryResponse(
    Long categoryId,
    String name
) {
    public static CategoryResponse fromEntity(Category category) {
        return new CategoryResponse(
            category.getCategoryId(),
            category.getName()
        );
    }
}
