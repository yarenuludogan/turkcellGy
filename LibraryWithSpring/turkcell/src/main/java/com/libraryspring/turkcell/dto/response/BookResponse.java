package com.libraryspring.turkcell.dto.response;

import com.libraryspring.turkcell.entity.Book;

/**
 * BookResponse - Kitap bilgilerini döndürmek için response DTO'su
 * İlişkileri ID'ler olarak döndürür 
 */
public record BookResponse(
    Long bookId,
    String title,
    String isbn,
    Integer publishedYear,
    Integer pageCount,
    Long authorId,         // Author nesnesi değil, sadece ID
    Long publisherId,      // Publisher nesnesi değil, sadece ID
    Long categoryId        // Category nesnesi değil, sadece ID
) {
    public static BookResponse fromEntity(Book book) {
        return new BookResponse(
            book.getBookId(),
            book.getTitle(),
            book.getIsbn(),
            book.getPublishedYear(),
            book.getPageCount(),
            book.getAuthor() != null ? book.getAuthor().getAuthorId() : null,
            book.getPublisher() != null ? book.getPublisher().getPublisherId() : null,
            book.getCategory() != null ? book.getCategory().getCategoryId() : null
        );
    }
}
