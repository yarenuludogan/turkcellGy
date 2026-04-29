package com.turkcell.spring_starter.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turkcell.spring_starter.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>
{ 
    Optional<Product> findByName(String name);
}
