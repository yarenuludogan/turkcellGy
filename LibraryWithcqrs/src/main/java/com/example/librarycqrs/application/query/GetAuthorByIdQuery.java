package com.example.librarycqrs.application.query;

import com.example.librarycqrs.application.mediator.Request;
import com.example.librarycqrs.application.view.AuthorView;

public record GetAuthorByIdQuery(Long authorId) implements Request<AuthorView> {
}
