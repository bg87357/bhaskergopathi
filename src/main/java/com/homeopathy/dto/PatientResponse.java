package com.homeopathy.dto;

import com.homeopathy.entity.Patient;
import java.time.LocalDate;

public class PatientResponse {
    private Long id;
    private String name;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String email;
    private String address;
    private String gender;
    private String medicalHistory;
    private String emergencyContact;
    private String clinicName;
    private Long clinicId;

    // Constructor from Patient entity
    public PatientResponse(Patient patient) {
        this.id = patient.getId();
        this.name = patient.getName();
        this.dateOfBirth = patient.getDateOfBirth();
        this.phoneNumber = patient.getPhoneNumber();
        this.email = patient.getEmail();
        this.address = patient.getAddress();
        this.gender = patient.getGender() != null ? patient.getGender().toString() : null;
        this.medicalHistory = patient.getMedicalHistory();
        this.emergencyContact = patient.getEmergencyContact();
        this.clinicName = patient.getClinic() != null ? patient.getClinic().getName() : null;
        this.clinicId = patient.getClinic() != null ? patient.getClinic().getId() : null;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }

    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }

    public String getClinicName() { return clinicName; }
    public void setClinicName(String clinicName) { this.clinicName = clinicName; }

    public Long getClinicId() { return clinicId; }
    public void setClinicId(Long clinicId) { this.clinicId = clinicId; }
}