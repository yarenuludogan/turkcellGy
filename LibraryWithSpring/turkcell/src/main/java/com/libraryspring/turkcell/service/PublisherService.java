package com.libraryspring.turkcell.service;

import com.libraryspring.turkcell.entity.Publisher;
import com.libraryspring.turkcell.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;


@Service
public class PublisherService {
    
    @Autowired
    private PublisherRepository publisherRepository;
    
    @Transactional
    public Publisher save(Publisher publisher) {
        return publisherRepository.save(publisher);
    }
    
    public List<Publisher> findAll() {
        return publisherRepository.findAll();
    }
    
    public Optional<Publisher> findById(Long id) {
        return publisherRepository.findById(id);
    }
    
    public List<Publisher> findByName(String name) {
        return publisherRepository.findByName(name);
    }
    
    public List<Publisher> findByAddress(String address) {
        return publisherRepository.findByAddress(address);
    }
    
    @Transactional
    public Publisher update(Long id, Publisher updatedPublisher) {
        Optional<Publisher> existing = findById(id);
        if (existing.isPresent()) {
            Publisher publisher = existing.get();
            publisher.setName(updatedPublisher.getName());
            publisher.setAddress(updatedPublisher.getAddress());
            return save(publisher);
        }
        throw new RuntimeException("Publisher with ID " + id + " not found");
    }
    
    @Transactional
    public void delete(Long id) {
        publisherRepository.deleteById(id);
    }
}
