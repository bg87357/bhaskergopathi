package com.homeopathy.controller;

import com.homeopathy.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/appointments/csv")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<byte[]> exportAppointmentsCsv(
            @RequestParam(required = false) Long clinicId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        byte[] csvData = reportService.generateAppointmentsCsvReport(clinicId, startDate, endDate);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "appointments-report.csv");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(csvData);
    }

    @GetMapping("/patients/csv")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<byte[]> exportPatientsCsv(@RequestParam(required = false) Long clinicId) {
        byte[] csvData = reportService.generatePatientsCsvReport(clinicId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "patients-report.csv");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(csvData);
    }

    @GetMapping("/daily-summary")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR') or hasRole('RECEPTIONIST')")
    public ResponseEntity<String> getDailySummary(
            @RequestParam(required = false) Long clinicId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        if (date == null) {
            date = LocalDate.now();
        }
        
        String summary = reportService.generateDailySummary(clinicId, date);
        return ResponseEntity.ok(summary);
    }
}