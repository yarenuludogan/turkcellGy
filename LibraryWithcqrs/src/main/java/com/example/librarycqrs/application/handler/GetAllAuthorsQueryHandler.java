package com.example.librarycqrs.application.handler;

import com.example.librarycqrs.application.mediator.RequestHandler;
import com.example.librarycqrs.application.query.GetAllAuthorsQuery;
import com.example.librarycqrs.application.view.AuthorView;
import com.example.librarycqrs.domain.Author;
import com.example.librarycqrs.domain.repository.AuthorRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class GetAllAuthorsQueryHandler implements RequestHandler<GetAllAuthorsQuery, List<AuthorView>> {

    private final AuthorRepository authorRepository;

    public GetAllAuthorsQueryHandler(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Class<GetAllAuthorsQuery> requestType() {
        return GetAllAuthorsQuery.class;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorView> handle(GetAllAuthorsQuery request) {
        return authorRepository.findAll().stream()
                .map(author -> new AuthorView(author.getId(), author.getName()))
                .toList();
    }
}
