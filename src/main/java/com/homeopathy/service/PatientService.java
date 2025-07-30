package com.homeopathy.service;

import com.homeopathy.entity.Patient;
import com.homeopathy.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
    }

    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    public List<Patient> getPatientsByClinic(Long clinicId) {
        return patientRepository.findByClinicId(clinicId);
    }

    public List<Patient> searchPatients(String query, Long clinicId) {
        if (clinicId != null) {
            return patientRepository.findByClinicIdAndSearchTerm(clinicId, query);
        } else {
            return patientRepository.findByNameContainingIgnoreCase(query);
        }
    }
}