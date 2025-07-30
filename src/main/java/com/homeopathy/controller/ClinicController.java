package com.homeopathy.controller;

import com.homeopathy.entity.Clinic;
import com.homeopathy.service.ClinicService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clinics")
@CrossOrigin(origins = "*")
public class ClinicController {

    @Autowired
    private ClinicService clinicService;

    @GetMapping
    public ResponseEntity<List<Clinic>> getAllClinics() {
        List<Clinic> clinics = clinicService.getAllClinics();
        return ResponseEntity.ok(clinics);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Clinic> getClinicById(@PathVariable Long id) {
        Clinic clinic = clinicService.getClinicById(id);
        return ResponseEntity.ok(clinic);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Clinic> createClinic(@Valid @RequestBody Clinic clinic) {
        Clinic savedClinic = clinicService.saveClinic(clinic);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedClinic);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Clinic> updateClinic(@PathVariable Long id, @Valid @RequestBody Clinic clinic) {
        clinic.setId(id);
        Clinic updatedClinic = clinicService.saveClinic(clinic);
        return ResponseEntity.ok(updatedClinic);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteClinic(@PathVariable Long id) {
        clinicService.deleteClinic(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Clinic>> searchClinics(@RequestParam String query) {
        List<Clinic> clinics = clinicService.searchClinics(query);
        return ResponseEntity.ok(clinics);
    }
}