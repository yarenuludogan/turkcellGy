package com.turkcell.spring_starter.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turkcell.spring_starter.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, UUID>
{ 
}
