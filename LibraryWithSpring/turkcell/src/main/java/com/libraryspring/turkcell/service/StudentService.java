package com.libraryspring.turkcell.service;

import com.libraryspring.turkcell.entity.Student;
import com.libraryspring.turkcell.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;


@Service
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Transactional
    public Student save(Student student) {
        return studentRepository.save(student);
    }
    
    public List<Student> findAll() {
        return studentRepository.findAll();
    }
    
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }
    
    public List<Student> findByName(String name) {
        return studentRepository.findByName(name);
    }
    
    public Optional<Student> findByEmail(String email) {
        return studentRepository.findByEmail(email);
    }
    
    public List<Student> findByPhone(String phone) {
        return studentRepository.findByPhone(phone);
    }
    
    @Transactional
    public Student update(Long id, Student updatedStudent) {
        Optional<Student> existing = findById(id);
        if (existing.isPresent()) {
            Student student = existing.get();
            student.setName(updatedStudent.getName());
            student.setEmail(updatedStudent.getEmail());
            student.setPhone(updatedStudent.getPhone());
            return save(student);
        }
        throw new RuntimeException("Student with ID " + id + " not found");
    }
    
    @Transactional
    public void delete(Long id) {
        studentRepository.deleteById(id);
    }
}
