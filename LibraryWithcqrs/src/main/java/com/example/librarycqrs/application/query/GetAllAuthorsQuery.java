package com.example.librarycqrs.application.query;

import com.example.librarycqrs.application.mediator.Request;
import com.example.librarycqrs.application.view.AuthorView;

import java.util.List;

public record GetAllAuthorsQuery() implements Request<List<AuthorView>> {
}
