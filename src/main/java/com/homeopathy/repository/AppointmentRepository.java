package com.homeopathy.repository;

import com.homeopathy.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByClinicId(Long clinicId);
    List<Appointment> findByDoctorId(Long doctorId);
    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findByAppointmentDate(LocalDate date);
    List<Appointment> findByStatus(Appointment.Status status);
    
    @Query("SELECT a FROM Appointment a WHERE a.clinic.id = :clinicId AND a.appointmentDate = :date")
    List<Appointment> findByClinicIdAndDate(@Param("clinicId") Long clinicId, 
                                          @Param("date") LocalDate date);
    
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate BETWEEN :startDate AND :endDate")
    List<Appointment> findByDateRange(@Param("startDate") LocalDate startDate, 
                                    @Param("endDate") LocalDate endDate);
    
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.clinic.id = :clinicId")
    Long countByClinicId(@Param("clinicId") Long clinicId);
}