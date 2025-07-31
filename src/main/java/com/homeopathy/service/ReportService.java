package com.homeopathy.service;

import com.homeopathy.entity.Appointment;
import com.homeopathy.entity.Patient;
import com.homeopathy.repository.AppointmentRepository;
import com.homeopathy.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    public byte[] generateAppointmentsCsvReport(Long clinicId, LocalDate startDate, LocalDate endDate) {
        List<Appointment> appointments;
        
        if (startDate != null && endDate != null) {
            appointments = appointmentRepository.findByDateRange(startDate, endDate);
        } else if (clinicId != null) {
            appointments = appointmentRepository.findByClinicId(clinicId);
        } else {
            appointments = appointmentRepository.findAll();
        }
        
        // Filter by clinic if specified
        if (clinicId != null && (startDate != null || endDate != null)) {
            appointments = appointments.stream()
                    .filter(apt -> apt.getClinic().getId().equals(clinicId))
                    .toList();
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(outputStream);

        // CSV Header
        writer.println("Date,Time,Patient Name,Patient Phone,Doctor Name,Clinic Name,Status,Notes,Prescription");

        // CSV Data
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (Appointment appointment : appointments) {
            writer.printf("%s,%s,%s,%s,%s,%s,%s,\"%s\",\"%s\"%n",
                    appointment.getAppointmentDate().format(dateFormatter),
                    appointment.getAppointmentTime().toString(),
                    appointment.getPatient().getName(),
                    appointment.getPatient().getPhoneNumber() != null ? appointment.getPatient().getPhoneNumber() : "",
                    appointment.getDoctor().getName(),
                    appointment.getClinic().getName(),
                    appointment.getStatus().toString(),
                    appointment.getNotes() != null ? appointment.getNotes().replace("\"", "\"\"") : "",
                    appointment.getPrescription() != null ? appointment.getPrescription().replace("\"", "\"\"") : ""
            );
        }

        writer.flush();
        writer.close();
        return outputStream.toByteArray();
    }

    public byte[] generatePatientsCsvReport(Long clinicId) {
        List<Patient> patients;
        
        if (clinicId != null) {
            patients = patientRepository.findByClinicId(clinicId);
        } else {
            patients = patientRepository.findAll();
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(outputStream);

        // CSV Header
        writer.println("Name,Date of Birth,Phone,Email,Address,Gender,Clinic,Emergency Contact,Medical History");

        // CSV Data
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (Patient patient : patients) {
            writer.printf("%s,%s,%s,%s,\"%s\",%s,%s,%s,\"%s\"%n",
                    patient.getName(),
                    patient.getDateOfBirth() != null ? patient.getDateOfBirth().format(dateFormatter) : "",
                    patient.getPhoneNumber() != null ? patient.getPhoneNumber() : "",
                    patient.getEmail() != null ? patient.getEmail() : "",
                    patient.getAddress() != null ? patient.getAddress().replace("\"", "\"\"") : "",
                    patient.getGender() != null ? patient.getGender().toString() : "",
                    patient.getClinic().getName(),
                    patient.getEmergencyContact() != null ? patient.getEmergencyContact() : "",
                    patient.getMedicalHistory() != null ? patient.getMedicalHistory().replace("\"", "\"\"") : ""
            );
        }

        writer.flush();
        writer.close();
        return outputStream.toByteArray();
    }

    public String generateDailySummary(Long clinicId, LocalDate date) {
        List<Appointment> appointments;
        
        if (clinicId != null) {
            appointments = appointmentRepository.findByClinicIdAndDate(clinicId, date);
        } else {
            appointments = appointmentRepository.findByAppointmentDate(date);
        }

        StringBuilder summary = new StringBuilder();
        summary.append("Daily Appointment Summary for ").append(date.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))).append("\n");
        summary.append("=".repeat(60)).append("\n\n");

        if (appointments.isEmpty()) {
            summary.append("No appointments scheduled for this date.\n");
            return summary.toString();
        }

        // Group by status
        long scheduled = appointments.stream().mapToLong(a -> a.getStatus() == Appointment.Status.SCHEDULED ? 1 : 0).sum();
        long completed = appointments.stream().mapToLong(a -> a.getStatus() == Appointment.Status.COMPLETED ? 1 : 0).sum();
        long cancelled = appointments.stream().mapToLong(a -> a.getStatus() == Appointment.Status.CANCELLED ? 1 : 0).sum();
        long noShow = appointments.stream().mapToLong(a -> a.getStatus() == Appointment.Status.NO_SHOW ? 1 : 0).sum();

        summary.append("Summary:\n");
        summary.append("- Total Appointments: ").append(appointments.size()).append("\n");
        summary.append("- Scheduled: ").append(scheduled).append("\n");
        summary.append("- Completed: ").append(completed).append("\n");
        summary.append("- Cancelled: ").append(cancelled).append("\n");
        summary.append("- No Show: ").append(noShow).append("\n\n");

        summary.append("Appointment Details:\n");
        summary.append("-".repeat(60)).append("\n");

        appointments.sort((a1, a2) -> a1.getAppointmentTime().compareTo(a2.getAppointmentTime()));

        for (Appointment appointment : appointments) {
            summary.append(String.format("%s | %s | %s | Dr. %s | %s\n",
                    appointment.getAppointmentTime().toString(),
                    appointment.getPatient().getName(),
                    appointment.getStatus().toString(),
                    appointment.getDoctor().getName(),
                    appointment.getClinic().getName()
            ));
        }

        return summary.toString();
    }
}