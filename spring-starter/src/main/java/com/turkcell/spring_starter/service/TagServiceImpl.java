package com.turkcell.spring_starter.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.turkcell.spring_starter.dto.request.CreateTagRequest;
import com.turkcell.spring_starter.dto.response.CreatedTagResponse;
import com.turkcell.spring_starter.dto.response.GetTagResponse;
import com.turkcell.spring_starter.dto.response.ListTagResponse;
import com.turkcell.spring_starter.dto.request.UpdateTagRequest;
import com.turkcell.spring_starter.dto.response.UpdatedTagResponse;
import com.turkcell.spring_starter.entity.Tag;
import com.turkcell.spring_starter.exception.EntityAlreadyExistsException;
import com.turkcell.spring_starter.exception.EntityNotFoundException;
import com.turkcell.spring_starter.repository.TagRepository;

@Service
public class TagServiceImpl {
    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public CreatedTagResponse create(CreateTagRequest createTagRequest) {
        if (tagRepository.findByName(createTagRequest.getName()).isPresent()) {
            throw new EntityAlreadyExistsException("Tag with name '" + createTagRequest.getName() + "' already exists");
        }

        Tag tag = new Tag();
        tag.setName(createTagRequest.getName());

        tag = tagRepository.save(tag);

        CreatedTagResponse response = new CreatedTagResponse();
        response.setId(tag.getId());
        response.setName(tag.getName());

        return response;
    }

    public List<ListTagResponse> getAll() {
        List<Tag> tags = tagRepository.findAll();

        return tags.stream().map(tag -> {
            ListTagResponse listTagResponse = new ListTagResponse();
            listTagResponse.setId(tag.getId());
            listTagResponse.setName(tag.getName());
            return listTagResponse;
        }).collect(Collectors.toList());
    }

    public GetTagResponse getById(UUID id) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tag not found with id: " + id));

        GetTagResponse response = new GetTagResponse();
        response.setId(tag.getId());
        response.setName(tag.getName());

        return response;
    }

    public UpdatedTagResponse update(UUID id, UpdateTagRequest updateTagRequest) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tag not found with id: " + id));

        // Check if another tag with the same name exists
        tagRepository.findByName(updateTagRequest.getName()).ifPresent(existingTag -> {
            if (!existingTag.getId().equals(id)) {
                throw new EntityAlreadyExistsException("Tag with name '" + updateTagRequest.getName() + "' already exists");
            }
        });

        tag.setName(updateTagRequest.getName());
        tag = tagRepository.save(tag);

        UpdatedTagResponse response = new UpdatedTagResponse();
        response.setId(tag.getId());
        response.setName(tag.getName());

        return response;
    }

    public void delete(UUID id) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tag not found with id: " + id));

        tagRepository.delete(tag);
    }
}
