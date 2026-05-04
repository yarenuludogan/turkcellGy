package com.example.librarycqrs.application.query;

import com.example.librarycqrs.application.mediator.Request;
import com.example.librarycqrs.application.view.BookView;

public record GetBookByIdQuery(Long id) implements Request<BookView> {
}
