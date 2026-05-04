package com.example.librarycqrs.web;

import com.example.librarycqrs.application.command.CreateAuthorCommand;
import com.example.librarycqrs.application.mediator.Mediator;
import com.example.librarycqrs.web.dto.CreateAuthorRequest;
import com.example.librarycqrs.web.dto.CreatedResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
}
