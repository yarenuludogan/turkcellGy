package com.libraryspring.turkcell.dto.response;

import com.libraryspring.turkcell.entity.Author;

/**
 * AuthorResponse - Yazar bilgilerini döndürmek için response DTO'su
 * Sadece gerekli bilgileri içerir 
 */
public record AuthorResponse(
    Long authorId,
    String name,
    String country
) {
    // Entity'den DTO'ya dönüştürme
    public static AuthorResponse fromEntity(Author author) {
        return new AuthorResponse(
            author.getAuthorId(),
            author.getName(),
            author.getCountry()
        );
    }
}
