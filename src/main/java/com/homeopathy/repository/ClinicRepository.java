package com.homeopathy.repository;

import com.homeopathy.entity.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    List<Clinic> findByNameContainingIgnoreCase(String name);
    List<Clinic> findByLocationContainingIgnoreCase(String location);
}