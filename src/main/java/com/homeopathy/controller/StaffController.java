package com.homeopathy.controller;

import com.homeopathy.entity.Staff;
import com.homeopathy.service.StaffService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
@CrossOrigin(origins = "*")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @GetMapping
    public ResponseEntity<List<Staff>> getAllStaff() {
        List<Staff> staff = staffService.getAllStaff();
        return ResponseEntity.ok(staff);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Staff> getStaffById(@PathVariable Long id) {
        Staff staff = staffService.getStaffById(id);
        return ResponseEntity.ok(staff);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Staff> createStaff(@Valid @RequestBody Staff staff) {
        Staff savedStaff = staffService.saveStaff(staff);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStaff);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Staff> updateStaff(@PathVariable Long id, @Valid @RequestBody Staff staff) {
        staff.setId(id);
        Staff updatedStaff = staffService.saveStaff(staff);
        return ResponseEntity.ok(updatedStaff);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/clinic/{clinicId}")
    public ResponseEntity<List<Staff>> getStaffByClinic(@PathVariable Long clinicId) {
        List<Staff> staff = staffService.getStaffByClinic(clinicId);
        return ResponseEntity.ok(staff);
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<Staff>> getStaffByRole(@PathVariable Staff.Role role) {
        List<Staff> staff = staffService.getStaffByRole(role);
        return ResponseEntity.ok(staff);
    }

    @GetMapping("/clinic/{clinicId}/role/{role}")
    public ResponseEntity<List<Staff>> getStaffByClinicAndRole(
            @PathVariable Long clinicId, 
            @PathVariable Staff.Role role) {
        List<Staff> staff = staffService.getStaffByClinicAndRole(clinicId, role);
        return ResponseEntity.ok(staff);
    }
}