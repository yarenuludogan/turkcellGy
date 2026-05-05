package com.libraryspring.turkcell.dto.response;

import com.libraryspring.turkcell.entity.Staff;
import java.time.LocalDate;

/**
 * StaffResponse - Personel bilgilerini döndürmek için response DTO'su
 */
public record StaffResponse(
    Long staffId,
    String name,
    String role,
    LocalDate hireDate
) {
    public static StaffResponse fromEntity(Staff staff) {
        return new StaffResponse(
            staff.getStaffId(),
            staff.getName(),
            staff.getRole(),
            staff.getHireDate()
        );
    }
}
