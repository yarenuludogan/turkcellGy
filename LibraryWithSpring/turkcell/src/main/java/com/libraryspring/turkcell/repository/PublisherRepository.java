package com.libraryspring.turkcell.repository;

import com.libraryspring.turkcell.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    
    List<Publisher> findByName(String name);
    
  
    List<Publisher> findByAddress(String address);
}
