package com.example.librarycqrs.application.handler;

import com.example.librarycqrs.application.command.CreateAuthorCommand;
import com.example.librarycqrs.application.mediator.RequestHandler;
import com.example.librarycqrs.domain.Author;
import com.example.librarycqrs.domain.repository.AuthorRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateAuthorCommandHandler implements RequestHandler<CreateAuthorCommand, Long> {

    private final AuthorRepository authorRepository;

    public CreateAuthorCommandHandler(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Class<CreateAuthorCommand> requestType() {
        return CreateAuthorCommand.class;
    }

    @Override
    @Transactional
    public Long handle(CreateAuthorCommand request) {
        Author saved = authorRepository.save(new Author(request.name()));
        return saved.getId();
    }
}
