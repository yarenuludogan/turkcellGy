package com.libraryspring.turkcell.controller;

import com.libraryspring.turkcell.dto.request.StaffCreateRequest;
import com.libraryspring.turkcell.dto.response.StaffResponse;
import com.libraryspring.turkcell.entity.Staff;
import com.libraryspring.turkcell.service.StaffService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/staff")
public class StaffController {
    
    @Autowired
    private StaffService staffService;
    
    @PostMapping
    public ResponseEntity<StaffResponse> createStaff(@Valid @RequestBody StaffCreateRequest request) {
        Staff staff = new Staff(request.name(), request.role(), request.hireDate());
        Staff saved = staffService.save(staff);
        return ResponseEntity.status(HttpStatus.CREATED).body(StaffResponse.fromEntity(saved));
    }
    
    @GetMapping
    public ResponseEntity<List<StaffResponse>> getAllStaff() {
        List<Staff> staffList = staffService.findAll();
        List<StaffResponse> responses = staffList.stream()
            .map(StaffResponse::fromEntity)
            .toList();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<StaffResponse> getStaffById(@PathVariable Long id) {
        return staffService.findById(id)
            .map(s -> ResponseEntity.ok(StaffResponse.fromEntity(s)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<StaffResponse> updateStaff(
            @PathVariable Long id,
            @Valid @RequestBody StaffCreateRequest request) {
        Staff updatedStaff = new Staff(request.name(), request.role(), request.hireDate());
        Staff saved = staffService.update(id, updatedStaff);
        return ResponseEntity.ok(StaffResponse.fromEntity(saved));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        staffService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
