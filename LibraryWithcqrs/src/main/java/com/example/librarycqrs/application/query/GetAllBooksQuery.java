package com.example.librarycqrs.application.query;

import com.example.librarycqrs.application.mediator.Request;
import com.example.librarycqrs.application.view.BookView;

import java.util.List;

public record GetAllBooksQuery() implements Request<List<BookView>> {
}
