package com.homeopathy.repository;

import com.homeopathy.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByEmail(String email);
    List<Staff> findByClinicId(Long clinicId);
    List<Staff> findByRole(Staff.Role role);
    List<Staff> findByClinicIdAndRole(Long clinicId, Staff.Role role);
}