package com.example.librarycqrs.web;

import com.example.librarycqrs.application.command.CreateBookCommand;
import com.example.librarycqrs.application.mediator.Mediator;
import com.example.librarycqrs.application.query.GetAllBooksQuery;
import com.example.librarycqrs.application.query.GetBookByIdQuery;
import com.example.librarycqrs.application.view.BookView;
import com.example.librarycqrs.web.dto.CreateBookRequest;
import com.example.librarycqrs.web.dto.CreatedResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final Mediator mediator;

    public BookController(Mediator mediator) {
        this.mediator = mediator;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedResponse createBook(@Valid @RequestBody CreateBookRequest request) {
        Long bookId = mediator.send(
                new CreateBookCommand(
                        request.title(),
                        request.isbn(),
                        request.publishedYear(),
                        request.authorId()
                )
        );
        return new CreatedResponse(bookId);
    }

    @GetMapping("/{id}")
    public BookView getBookById(@PathVariable Long id) {
        return mediator.send(new GetBookByIdQuery(id));
    }

    @GetMapping
    public List<BookView> getAllBooks() {
        return mediator.send(new GetAllBooksQuery());
    }
}
