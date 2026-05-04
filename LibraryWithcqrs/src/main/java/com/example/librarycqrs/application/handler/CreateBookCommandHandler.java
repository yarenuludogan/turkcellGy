package com.example.librarycqrs.application.handler;

import com.example.librarycqrs.application.command.CreateBookCommand;
import com.example.librarycqrs.application.mediator.RequestHandler;
import com.example.librarycqrs.domain.Author;
import com.example.librarycqrs.domain.Book;
import com.example.librarycqrs.domain.repository.AuthorRepository;
import com.example.librarycqrs.domain.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Component
public class CreateBookCommandHandler implements RequestHandler<CreateBookCommand, Long> {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public CreateBookCommandHandler(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public Class<CreateBookCommand> requestType() {
        return CreateBookCommand.class;
    }

    @Override
    @Transactional
    public Long handle(CreateBookCommand request) {
        Author author = authorRepository.findById(request.authorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));

        Book savedBook = bookRepository.save(
                new Book(request.title(), request.isbn(), request.publishedYear(), author)
        );

        return savedBook.getId();
    }
}
