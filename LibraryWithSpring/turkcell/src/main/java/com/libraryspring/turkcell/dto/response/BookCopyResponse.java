package com.libraryspring.turkcell.dto.response;

import com.libraryspring.turkcell.entity.BookCopy;

/**
 * BookCopyResponse - Kitap kopyası bilgilerini döndürmek için response DTO'su
 */
public record BookCopyResponse(
    Long copyId,
    Long bookId,           
    String status,
    String shelfLocation
) {
    public static BookCopyResponse fromEntity(BookCopy bookCopy) {
        return new BookCopyResponse(
            bookCopy.getCopyId(),
            bookCopy.getBook() != null ? bookCopy.getBook().getBookId() : null,
            bookCopy.getStatus(),
            bookCopy.getShelfLocation()
        );
    }
}
