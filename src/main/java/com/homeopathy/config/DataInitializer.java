package com.homeopathy.config;

import com.homeopathy.entity.*;
import com.homeopathy.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Only initialize if database is empty
        if (clinicRepository.count() == 0) {
            initializeData();
        }
    }

    private void initializeData() {
        // Create Clinics
        Clinic centralClinic = new Clinic("HomeoClinic Central", "123 Main Street, Downtown, City Center");
        centralClinic.setPhoneNumber("+1-555-0101");
        centralClinic.setEmail("central@homeoclinic.com");

        Clinic northClinic = new Clinic("HomeoClinic North", "456 Oak Avenue, North District, Suburb Area");
        northClinic.setPhoneNumber("+1-555-0102");
        northClinic.setEmail("north@homeoclinic.com");

        Clinic southClinic = new Clinic("HomeoClinic South", "789 Pine Road, South Valley, Residential Zone");
        southClinic.setPhoneNumber("+1-555-0103");
        southClinic.setEmail("south@homeoclinic.com");

        clinicRepository.saveAll(Arrays.asList(centralClinic, northClinic, southClinic));

        // Create Staff
        Staff admin = new Staff("Admin User", "admin@clinic.com", Staff.Role.ADMIN, centralClinic);
        admin.setPhoneNumber("+1-555-1000");
        admin.setSpecialization("System Administration");

        Staff doctor1 = new Staff("Dr. Sarah Johnson", "sarah.johnson@homeoclinic.com", Staff.Role.DOCTOR, centralClinic);
        doctor1.setPhoneNumber("+1-555-1001");
        doctor1.setSpecialization("General Homeopathy, Chronic Diseases");

        Staff doctor2 = new Staff("Dr. Michael Chen", "michael.chen@homeoclinic.com", Staff.Role.DOCTOR, centralClinic);
        doctor2.setPhoneNumber("+1-555-1002");
        doctor2.setSpecialization("Pediatric Homeopathy, Allergies");

        Staff receptionist1 = new Staff("Emily Davis", "emily.davis@homeoclinic.com", Staff.Role.RECEPTIONIST, centralClinic);
        receptionist1.setPhoneNumber("+1-555-1003");

        Staff doctor3 = new Staff("Dr. Robert Wilson", "robert.wilson@homeoclinic.com", Staff.Role.DOCTOR, northClinic);
        doctor3.setPhoneNumber("+1-555-2001");
        doctor3.setSpecialization("Mental Health, Stress Management");

        Staff doctor4 = new Staff("Dr. Lisa Anderson", "lisa.anderson@homeoclinic.com", Staff.Role.DOCTOR, northClinic);
        doctor4.setPhoneNumber("+1-555-2002");
        doctor4.setSpecialization("Women Health, Hormonal Issues");

        Staff receptionist2 = new Staff("James Miller", "james.miller@homeoclinic.com", Staff.Role.RECEPTIONIST, northClinic);
        receptionist2.setPhoneNumber("+1-555-2003");

        Staff doctor5 = new Staff("Dr. Maria Garcia", "maria.garcia@homeoclinic.com", Staff.Role.DOCTOR, southClinic);
        doctor5.setPhoneNumber("+1-555-3001");
        doctor5.setSpecialization("Digestive Disorders, Nutrition");

        Staff doctor6 = new Staff("Dr. David Brown", "david.brown@homeoclinic.com", Staff.Role.DOCTOR, southClinic);
        doctor6.setPhoneNumber("+1-555-3002");
        doctor6.setSpecialization("Respiratory Issues, Immunity");

        Staff receptionist3 = new Staff("Anna Thompson", "anna.thompson@homeoclinic.com", Staff.Role.RECEPTIONIST, southClinic);
        receptionist3.setPhoneNumber("+1-555-3003");

        staffRepository.saveAll(Arrays.asList(admin, doctor1, doctor2, receptionist1, doctor3, doctor4, receptionist2, doctor5, doctor6, receptionist3));

        // Create Users
        String hashedPassword = passwordEncoder.encode("admin123");
        String doctorPassword = passwordEncoder.encode("doctor123");
        String receptionPassword = passwordEncoder.encode("reception123");

        User adminUser = new User("admin@clinic.com", hashedPassword, User.Role.ADMIN);
        adminUser.setStaff(admin);

        User doctorUser = new User("doctor@clinic.com", doctorPassword, User.Role.DOCTOR);
        doctorUser.setStaff(doctor1);

        User receptionUser = new User("reception@clinic.com", receptionPassword, User.Role.RECEPTIONIST);
        receptionUser.setStaff(receptionist1);

        userRepository.saveAll(Arrays.asList(adminUser, doctorUser, receptionUser));

        // Create Patients
        Patient patient1 = new Patient("John Smith", "+1-555-4001", centralClinic);
        patient1.setDateOfBirth(LocalDate.of(1985, 3, 15));
        patient1.setEmail("john.smith@email.com");
        patient1.setAddress("123 Elm Street, City Center");
        patient1.setGender(Patient.Gender.MALE);
        patient1.setMedicalHistory("Chronic headaches, stress-related symptoms. Previous treatments with conventional medicine showed limited success.");
        patient1.setEmergencyContact("Jane Smith - +1-555-4002");

        Patient patient2 = new Patient("Mary Johnson", "+1-555-4003", centralClinic);
        patient2.setDateOfBirth(LocalDate.of(1978, 7, 22));
        patient2.setEmail("mary.johnson@email.com");
        patient2.setAddress("456 Maple Avenue, Downtown");
        patient2.setGender(Patient.Gender.FEMALE);
        patient2.setMedicalHistory("Digestive issues, IBS symptoms. Seeking natural treatment alternatives.");
        patient2.setEmergencyContact("Robert Johnson - +1-555-4004");

        Patient patient3 = new Patient("Peter Wilson", "+1-555-4005", centralClinic);
        patient3.setDateOfBirth(LocalDate.of(1992, 11, 8));
        patient3.setEmail("peter.wilson@email.com");
        patient3.setAddress("789 Cedar Lane, City Center");
        patient3.setGender(Patient.Gender.MALE);
        patient3.setMedicalHistory("Anxiety, sleep disorders. Looking for holistic approach to mental health.");
        patient3.setEmergencyContact("Sarah Wilson - +1-555-4006");

        Patient patient4 = new Patient("Robert Brown", "+1-555-5001", northClinic);
        patient4.setDateOfBirth(LocalDate.of(1975, 12, 3));
        patient4.setEmail("robert.brown@email.com");
        patient4.setAddress("654 Spruce Street, North District");
        patient4.setGender(Patient.Gender.MALE);
        patient4.setMedicalHistory("High blood pressure, cardiovascular concerns. Family history of heart disease.");
        patient4.setEmergencyContact("Linda Brown - +1-555-5002");

        Patient patient5 = new Patient("Sarah Martinez", "+1-555-6001", southClinic);
        patient5.setDateOfBirth(LocalDate.of(1986, 4, 12));
        patient5.setEmail("sarah.martinez@email.com");
        patient5.setAddress("258 Ash Boulevard, South Valley");
        patient5.setGender(Patient.Gender.FEMALE);
        patient5.setMedicalHistory("Respiratory issues, asthma. Seeking complementary treatment options.");
        patient5.setEmergencyContact("Carlos Martinez - +1-555-6002");

        patientRepository.saveAll(Arrays.asList(patient1, patient2, patient3, patient4, patient5));

        // Create Appointments
        Appointment apt1 = new Appointment(patient1, doctor1, centralClinic, LocalDate.now(), LocalTime.of(9, 0));
        apt1.setNotes("Initial consultation for chronic headaches");

        Appointment apt2 = new Appointment(patient2, doctor2, centralClinic, LocalDate.now(), LocalTime.of(10, 30));
        apt2.setNotes("Follow-up for digestive issues");

        Appointment apt3 = new Appointment(patient3, doctor1, centralClinic, LocalDate.now().plusDays(1), LocalTime.of(9, 30));
        apt3.setNotes("Anxiety treatment consultation");

        Appointment apt4 = new Appointment(patient4, doctor3, northClinic, LocalDate.now().minusDays(1), LocalTime.of(14, 0));
        apt4.setStatus(Appointment.Status.COMPLETED);
        apt4.setNotes("Blood pressure monitoring");
        apt4.setPrescription("Crataegus 30C, twice daily");

        Appointment apt5 = new Appointment(patient5, doctor5, southClinic, LocalDate.now(), LocalTime.of(15, 0));
        apt5.setNotes("Respiratory assessment");

        appointmentRepository.saveAll(Arrays.asList(apt1, apt2, apt3, apt4, apt5));

        System.out.println("Sample data initialized successfully!");
    }
}