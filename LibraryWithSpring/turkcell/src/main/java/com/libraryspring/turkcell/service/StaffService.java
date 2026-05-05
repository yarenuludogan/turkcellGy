package com.libraryspring.turkcell.service;

import com.libraryspring.turkcell.entity.Staff;
import com.libraryspring.turkcell.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class StaffService {
    
    @Autowired
    private StaffRepository staffRepository;
    
    @Transactional
    public Staff save(Staff staff) {
        return staffRepository.save(staff);
    }
    
    public List<Staff> findAll() {
        return staffRepository.findAll();
    }
    
    public Optional<Staff> findById(Long id) {
        return staffRepository.findById(id);
    }
    
    public List<Staff> findByName(String name) {
        return staffRepository.findByName(name);
    }
    
    public List<Staff> findByRole(String role) {
        return staffRepository.findByRole(role);
    }
    
    @Transactional
    public Staff update(Long id, Staff updatedStaff) {
        Optional<Staff> existing = findById(id);
        if (existing.isPresent()) {
            Staff staff = existing.get();
            staff.setName(updatedStaff.getName());
            staff.setRole(updatedStaff.getRole());
            staff.setHireDate(updatedStaff.getHireDate());
            return save(staff);
        }
        throw new RuntimeException("Staff with ID " + id + " not found");
    }
    
    @Transactional
    public void delete(Long id) {
        staffRepository.deleteById(id);
    }
}
