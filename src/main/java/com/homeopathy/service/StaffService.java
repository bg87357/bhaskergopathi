package com.homeopathy.service;

import com.homeopathy.entity.Staff;
import com.homeopathy.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffService {

    @Autowired
    private StaffRepository staffRepository;

    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    public Staff getStaffById(Long id) {
        return staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found with id: " + id));
    }

    public Staff saveStaff(Staff staff) {
        return staffRepository.save(staff);
    }

    public void deleteStaff(Long id) {
        staffRepository.deleteById(id);
    }

    public List<Staff> getStaffByClinic(Long clinicId) {
        return staffRepository.findByClinicId(clinicId);
    }

    public List<Staff> getStaffByRole(Staff.Role role) {
        return staffRepository.findByRole(role);
    }

    public List<Staff> getStaffByClinicAndRole(Long clinicId, Staff.Role role) {
        return staffRepository.findByClinicIdAndRole(clinicId, role);
    }

    public Staff findByEmail(String email) {
        return staffRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Staff not found with email: " + email));
    }
}