package com.homeopathy.repository;

import com.homeopathy.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByClinicId(Long clinicId);
    List<Patient> findByNameContainingIgnoreCase(String name);
    List<Patient> findByPhoneNumberContaining(String phoneNumber);
    
    @Query("SELECT p FROM Patient p WHERE p.clinic.id = :clinicId AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "p.phoneNumber LIKE CONCAT('%', :searchTerm, '%'))")
    List<Patient> findByClinicIdAndSearchTerm(@Param("clinicId") Long clinicId, 
                                            @Param("searchTerm") String searchTerm);
}