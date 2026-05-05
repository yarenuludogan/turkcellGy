package com.libraryspring.turkcell.controller;

import com.libraryspring.turkcell.dto.request.StudentCreateRequest;
import com.libraryspring.turkcell.dto.response.StudentResponse;
import com.libraryspring.turkcell.entity.Student;
import com.libraryspring.turkcell.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/students")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody StudentCreateRequest request) {
        Student student = new Student(request.name(), request.email(), request.phone());
        Student saved = studentService.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(StudentResponse.fromEntity(saved));
    }
    
    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        List<Student> students = studentService.findAll();
        List<StudentResponse> responses = students.stream()
            .map(StudentResponse::fromEntity)
            .toList();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id) {
        return studentService.findById(id)
            .map(s -> ResponseEntity.ok(StudentResponse.fromEntity(s)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentCreateRequest request) {
        Student updatedStudent = new Student(request.name(), request.email(), request.phone());
        Student saved = studentService.update(id, updatedStudent);
        return ResponseEntity.ok(StudentResponse.fromEntity(saved));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
