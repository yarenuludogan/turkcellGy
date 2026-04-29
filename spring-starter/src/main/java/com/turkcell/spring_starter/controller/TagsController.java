package com.turkcell.spring_starter.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.spring_starter.dto.request.CreateTagRequest;
import com.turkcell.spring_starter.dto.response.CreatedTagResponse;
import com.turkcell.spring_starter.dto.response.GetTagResponse;
import com.turkcell.spring_starter.dto.response.ListTagResponse;
import com.turkcell.spring_starter.dto.request.UpdateTagRequest;
import com.turkcell.spring_starter.dto.response.UpdatedTagResponse;
import com.turkcell.spring_starter.service.TagServiceImpl;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/tags")
public class TagsController {
    private final TagServiceImpl tagServiceImpl;

    public TagsController(TagServiceImpl tagServiceImpl) {
        this.tagServiceImpl = tagServiceImpl;
    }

    @GetMapping
    public List<ListTagResponse> getAll() {
        return tagServiceImpl.getAll();
    }

    @GetMapping("/{id}")
    public GetTagResponse getById(@PathVariable UUID id) {
        return tagServiceImpl.getById(id);
    }

    @PutMapping("/{id}")
    public UpdatedTagResponse update(@PathVariable UUID id, @RequestBody @Valid UpdateTagRequest updateTagRequest) {
        return tagServiceImpl.update(id, updateTagRequest);
    }
    
    @PostMapping
    public CreatedTagResponse create(@RequestBody @Valid CreateTagRequest createTagRequest) {
        return tagServiceImpl.create(createTagRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        tagServiceImpl.delete(id);
    }
}

