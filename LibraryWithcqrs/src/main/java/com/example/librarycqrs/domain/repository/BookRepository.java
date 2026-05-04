package com.example.librarycqrs.domain.repository;

import com.example.librarycqrs.domain.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(attributePaths = "author")
    Optional<Book> findWithAuthorById(Long id);

    @Override
    @EntityGraph(attributePaths = "author")
    List<Book> findAll();
}
