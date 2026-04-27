package com.turkcell.spring_starter.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;


import com.turkcell.spring_starter.entity.Category;


public interface CategoryRepository extends JpaRepository<Category, UUID>
{ 
}

