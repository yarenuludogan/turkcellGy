package com.example.librarycqrs.application.handler;

import com.example.librarycqrs.application.command.DeleteAuthorCommand;
import com.example.librarycqrs.application.mediator.RequestHandler;
import com.example.librarycqrs.domain.repository.AuthorRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Component
public class DeleteAuthorCommandHandler implements RequestHandler<DeleteAuthorCommand, Void> {

    private final AuthorRepository authorRepository;

    public DeleteAuthorCommandHandler(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Class<DeleteAuthorCommand> requestType() {
        return DeleteAuthorCommand.class;
    }

    @Override
    @Transactional
    public Void handle(DeleteAuthorCommand request) {
        if (!authorRepository.existsById(request.authorId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found");
        }
        authorRepository.deleteById(request.authorId());
        return null;
    }
}
