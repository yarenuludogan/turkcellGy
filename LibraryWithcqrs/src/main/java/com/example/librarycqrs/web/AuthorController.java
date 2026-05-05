package com.example.librarycqrs.web;

import com.example.librarycqrs.application.command.CreateAuthorCommand;
import com.example.librarycqrs.application.command.DeleteAuthorCommand;
import com.example.librarycqrs.application.command.UpdateAuthorCommand;
import com.example.librarycqrs.application.mediator.Mediator;
import com.example.librarycqrs.application.query.GetAllAuthorsQuery;
import com.example.librarycqrs.application.query.GetAuthorByIdQuery;
import com.example.librarycqrs.application.view.AuthorView;
import com.example.librarycqrs.web.dto.CreateAuthorRequest;
import com.example.librarycqrs.web.dto.CreatedResponse;
import com.example.librarycqrs.web.dto.UpdateAuthorRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final Mediator mediator;

    public AuthorController(Mediator mediator) {
        this.mediator = mediator;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedResponse createAuthor(@Valid @RequestBody CreateAuthorRequest request) {
        Long authorId = mediator.send(new CreateAuthorCommand(request.name()));
        return new CreatedResponse(authorId);
    }

    @GetMapping
    public List<AuthorView> getAllAuthors() {
        return mediator.send(new GetAllAuthorsQuery());
    }

    @GetMapping("/{id}")
    public AuthorView getAuthorById(@PathVariable Long id) {
        return mediator.send(new GetAuthorByIdQuery(id));
    }

    @PutMapping("/{id}")
    public AuthorView updateAuthor(@PathVariable Long id, @Valid @RequestBody UpdateAuthorRequest request) {
        mediator.send(new UpdateAuthorCommand(id, request.name()));
        return mediator.send(new GetAuthorByIdQuery(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAuthor(@PathVariable Long id) {
        mediator.send(new DeleteAuthorCommand(id));
    }
}
