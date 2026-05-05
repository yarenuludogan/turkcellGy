package com.libraryspring.turkcell.repository;

import com.libraryspring.turkcell.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    

    List<Student> findByName(String name);
    
    Optional<Student> findByEmail(String email);
    
    List<Student> findByPhone(String phone);
}
