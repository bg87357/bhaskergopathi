package com.homeopathy.service;

import com.homeopathy.dto.DashboardStats;
import com.homeopathy.entity.Appointment;
import com.homeopathy.repository.AppointmentRepository;
import com.homeopathy.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    public DashboardStats getDashboardStats(Long clinicId, LocalDate date) {
        DashboardStats stats = new DashboardStats();

        if (date == null) {
            date = LocalDate.now();
        }

        if (clinicId != null) {
            stats.setTotalPatients(patientRepository.findByClinicId(clinicId).size());
            stats.setTotalAppointments(appointmentRepository.countByClinicId(clinicId));
            stats.setTodayAppointments(appointmentRepository.findByClinicIdAndDate(clinicId, date).size());
        } else {
            stats.setTotalPatients(patientRepository.count());
            stats.setTotalAppointments(appointmentRepository.count());
            stats.setTodayAppointments(appointmentRepository.findByAppointmentDate(date).size());
        }

        List<Appointment> allAppointments = clinicId != null ? 
            appointmentRepository.findByClinicId(clinicId) : 
            appointmentRepository.findAll();

        stats.setScheduledAppointments(allAppointments.stream()
                .mapToLong(a -> a.getStatus() == Appointment.Status.SCHEDULED ? 1 : 0).sum());
        stats.setCompletedAppointments(allAppointments.stream()
                .mapToLong(a -> a.getStatus() == Appointment.Status.COMPLETED ? 1 : 0).sum());
        stats.setCancelledAppointments(allAppointments.stream()
                .mapToLong(a -> a.getStatus() == Appointment.Status.CANCELLED ? 1 : 0).sum());

        return stats;
    }
}