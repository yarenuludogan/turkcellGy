package com.example.librarycqrs.application.handler;

import com.example.librarycqrs.application.mediator.RequestHandler;
import com.example.librarycqrs.application.query.GetBookByIdQuery;
import com.example.librarycqrs.application.view.BookView;
import com.example.librarycqrs.domain.Book;
import com.example.librarycqrs.domain.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Component
public class GetBookByIdQueryHandler implements RequestHandler<GetBookByIdQuery, BookView> {

    private final BookRepository bookRepository;

    public GetBookByIdQueryHandler(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Class<GetBookByIdQuery> requestType() {
        return GetBookByIdQuery.class;
    }

    @Override
    @Transactional(readOnly = true)
    public BookView handle(GetBookByIdQuery request) {
        Book book = bookRepository.findWithAuthorById(request.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));

        return new BookView(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getPublishedYear(),
                book.getAuthor().getId(),
                book.getAuthor().getName()
        );
    }
}
