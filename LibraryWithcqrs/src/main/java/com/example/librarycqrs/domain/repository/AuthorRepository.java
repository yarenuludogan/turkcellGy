package com.example.librarycqrs.domain.repository;

import com.example.librarycqrs.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
