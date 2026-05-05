package com.example.librarycqrs.application.handler;

import com.example.librarycqrs.application.mediator.RequestHandler;
import com.example.librarycqrs.application.query.GetAuthorByIdQuery;
import com.example.librarycqrs.application.view.AuthorView;
import com.example.librarycqrs.domain.Author;
import com.example.librarycqrs.domain.repository.AuthorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Component
public class GetAuthorByIdQueryHandler implements RequestHandler<GetAuthorByIdQuery, AuthorView> {

    private final AuthorRepository authorRepository;

    public GetAuthorByIdQueryHandler(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Class<GetAuthorByIdQuery> requestType() {
        return GetAuthorByIdQuery.class;
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorView handle(GetAuthorByIdQuery request) {
        Author author = authorRepository.findById(request.authorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));

        return new AuthorView(author.getId(), author.getName());
    }
}
