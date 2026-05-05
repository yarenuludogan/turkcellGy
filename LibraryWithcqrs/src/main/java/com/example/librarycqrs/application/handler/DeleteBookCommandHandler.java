package com.example.librarycqrs.application.handler;

import com.example.librarycqrs.application.command.DeleteBookCommand;
import com.example.librarycqrs.application.mediator.RequestHandler;
import com.example.librarycqrs.domain.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Component
public class DeleteBookCommandHandler implements RequestHandler<DeleteBookCommand, Void> {

    private final BookRepository bookRepository;

    public DeleteBookCommandHandler(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Class<DeleteBookCommand> requestType() {
        return DeleteBookCommand.class;
    }

    @Override
    @Transactional
    public Void handle(DeleteBookCommand request) {
        if (!bookRepository.existsById(request.bookId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }
        bookRepository.deleteById(request.bookId());
        return null;
    }
}
