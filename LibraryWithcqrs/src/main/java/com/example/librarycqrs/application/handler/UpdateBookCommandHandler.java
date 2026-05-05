package com.example.librarycqrs.application.handler;

import com.example.librarycqrs.application.command.UpdateBookCommand;
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
public class UpdateBookCommandHandler implements RequestHandler<UpdateBookCommand, Long> {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public UpdateBookCommandHandler(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public Class<UpdateBookCommand> requestType() {
        return UpdateBookCommand.class;
    }

    @Override
    @Transactional
    public Long handle(UpdateBookCommand request) {
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));

        Author author = authorRepository.findById(request.authorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));

        book.setTitle(request.title());
        book.setIsbn(request.isbn());
        book.setPublishedYear(request.publishedYear());
        book.setAuthor(author);

        Book saved = bookRepository.save(book);
        return saved.getId();
    }
}
