package com.libraryspring.turkcell.dto.response;

import com.libraryspring.turkcell.entity.Student;
import java.time.LocalDate;

/**
 * StudentResponse - Öğrenci bilgilerini döndürmek için response DTO'su
 */
public record StudentResponse(
    Long studentId,
    String name,
    String email,
    String phone,
    LocalDate registeredDate
) {
    public static StudentResponse fromEntity(Student student) {
        return new StudentResponse(
            student.getStudentId(),
            student.getName(),
            student.getEmail(),
            student.getPhone(),
            student.getRegisteredDate()
        );
    }
}
