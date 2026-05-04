package com.example.librarycqrs.application.handler;

import com.example.librarycqrs.application.mediator.RequestHandler;
import com.example.librarycqrs.application.query.GetAllBooksQuery;
import com.example.librarycqrs.application.view.BookView;
import com.example.librarycqrs.domain.Book;
import com.example.librarycqrs.domain.repository.BookRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class GetAllBooksQueryHandler implements RequestHandler<GetAllBooksQuery, List<BookView>> {

    private final BookRepository bookRepository;

    public GetAllBooksQueryHandler(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Class<GetAllBooksQuery> requestType() {
        return GetAllBooksQuery.class;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookView> handle(GetAllBooksQuery request) {
        return bookRepository.findAll().stream()
                .map(this::toView)
                .toList();
    }

    private BookView toView(Book book) {
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
