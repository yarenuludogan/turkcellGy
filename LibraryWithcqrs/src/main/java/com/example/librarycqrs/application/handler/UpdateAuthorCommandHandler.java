package com.example.librarycqrs.application.handler;

import com.example.librarycqrs.application.command.UpdateAuthorCommand;
import com.example.librarycqrs.application.mediator.RequestHandler;
import com.example.librarycqrs.domain.Author;
import com.example.librarycqrs.domain.repository.AuthorRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Component
public class UpdateAuthorCommandHandler implements RequestHandler<UpdateAuthorCommand, Long> {

    private final AuthorRepository authorRepository;

    public UpdateAuthorCommandHandler(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Class<UpdateAuthorCommand> requestType() {
        return UpdateAuthorCommand.class;
    }

    @Override
    @Transactional
    public Long handle(UpdateAuthorCommand request) {
        Author author = authorRepository.findById(request.authorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));

        author.setName(request.name());
        Author saved = authorRepository.save(author);
        return saved.getId();
    }
}
