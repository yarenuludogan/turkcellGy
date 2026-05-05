package com.libraryspring.turkcell.dto.response;

import com.libraryspring.turkcell.entity.Publisher;

/**
 * PublisherResponse - Yayıncı bilgilerini döndürmek için response DTO'su
 */
public record PublisherResponse(
    Long publisherId,
    String name,
    String address
) {
    public static PublisherResponse fromEntity(Publisher publisher) {
        return new PublisherResponse(
            publisher.getPublisherId(),
            publisher.getName(),
            publisher.getAddress()
        );
    }
}
