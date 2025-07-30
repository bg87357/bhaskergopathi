-- Database Schema for Homeopathic Clinic Management System

-- Create database (PostgreSQL)
-- CREATE DATABASE homeopathy_clinic;

-- Clinics table
CREATE TABLE IF NOT EXISTS clinics (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location TEXT NOT NULL,
    phone_number VARCHAR(20),
    email VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Staff table
CREATE TABLE IF NOT EXISTS staff (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    role VARCHAR(50) NOT NULL CHECK (role IN ('ADMIN', 'DOCTOR', 'RECEPTIONIST')),
    phone_number VARCHAR(20),
    specialization VARCHAR(255),
    clinic_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (clinic_id) REFERENCES clinics(id) ON DELETE CASCADE
);

-- Patients table
CREATE TABLE IF NOT EXISTS patients (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    date_of_birth DATE,
    phone_number VARCHAR(20),
    email VARCHAR(255),
    address TEXT,
    gender VARCHAR(10) CHECK (gender IN ('MALE', 'FEMALE', 'OTHER')),
    medical_history TEXT,
    emergency_contact VARCHAR(255),
    clinic_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (clinic_id) REFERENCES clinics(id) ON DELETE CASCADE
);

-- Users table for authentication
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL CHECK (role IN ('ADMIN', 'DOCTOR', 'RECEPTIONIST')),
    staff_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (staff_id) REFERENCES staff(id) ON DELETE SET NULL
);

-- Appointments table
CREATE TABLE IF NOT EXISTS appointments (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    clinic_id BIGINT NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED' CHECK (status IN ('SCHEDULED', 'COMPLETED', 'CANCELLED', 'NO_SHOW')),
    notes TEXT,
    prescription TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES staff(id) ON DELETE CASCADE,
    FOREIGN KEY (clinic_id) REFERENCES clinics(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_staff_clinic_id ON staff(clinic_id);
CREATE INDEX IF NOT EXISTS idx_staff_role ON staff(role);
CREATE INDEX IF NOT EXISTS idx_patients_clinic_id ON patients(clinic_id);
CREATE INDEX IF NOT EXISTS idx_patients_name ON patients(name);
CREATE INDEX IF NOT EXISTS idx_patients_phone ON patients(phone_number);
CREATE INDEX IF NOT EXISTS idx_appointments_patient_id ON appointments(patient_id);
CREATE INDEX IF NOT EXISTS idx_appointments_doctor_id ON appointments(doctor_id);
CREATE INDEX IF NOT EXISTS idx_appointments_clinic_id ON appointments(clinic_id);
CREATE INDEX IF NOT EXISTS idx_appointments_date ON appointments(appointment_date);
CREATE INDEX IF NOT EXISTS idx_appointments_status ON appointments(status);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);

-- Insert sample data
INSERT INTO clinics (name, location, phone_number, email) VALUES
('Central Homeopathy Clinic', '123 Main Street, Downtown, City', '+1-555-0101', 'central@homeopathy.com'),
('Wellness Homeopathy Center', '456 Oak Avenue, Suburb, City', '+1-555-0102', 'wellness@homeopathy.com'),
('Natural Healing Clinic', '789 Pine Road, Uptown, City', '+1-555-0103', 'natural@homeopathy.com');

-- Insert staff members
INSERT INTO staff (name, email, role, phone_number, specialization, clinic_id) VALUES
('Dr. Sarah Johnson', 'sarah.johnson@homeopathy.com', 'DOCTOR', '+1-555-1001', 'General Homeopathy', 1),
('Dr. Michael Chen', 'michael.chen@homeopathy.com', 'DOCTOR', '+1-555-1002', 'Pediatric Homeopathy', 1),
('Dr. Emily Davis', 'emily.davis@homeopathy.com', 'DOCTOR', '+1-555-1003', 'Women Health', 2),
('Dr. Robert Wilson', 'robert.wilson@homeopathy.com', 'DOCTOR', '+1-555-1004', 'Chronic Diseases', 2),
('Dr. Lisa Anderson', 'lisa.anderson@homeopathy.com', 'DOCTOR', '+1-555-1005', 'Mental Health', 3),
('Mary Smith', 'mary.smith@homeopathy.com', 'RECEPTIONIST', '+1-555-2001', NULL, 1),
('John Brown', 'john.brown@homeopathy.com', 'RECEPTIONIST', '+1-555-2002', NULL, 2),
('Anna Taylor', 'anna.taylor@homeopathy.com', 'RECEPTIONIST', '+1-555-2003', NULL, 3),
('Admin User', 'admin@clinic.com', 'ADMIN', '+1-555-0001', NULL, 1);

-- Insert users for authentication (passwords are hashed versions of: admin123, doctor123, reception123)
INSERT INTO users (email, password, role, staff_id) VALUES
('admin@clinic.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'ADMIN', 9),
('sarah.johnson@homeopathy.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'DOCTOR', 1),
('michael.chen@homeopathy.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'DOCTOR', 2),
('emily.davis@homeopathy.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'DOCTOR', 3),
('robert.wilson@homeopathy.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'DOCTOR', 4),
('lisa.anderson@homeopathy.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'DOCTOR', 5),
('mary.smith@homeopathy.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'RECEPTIONIST', 6),
('john.brown@homeopathy.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'RECEPTIONIST', 7),
('anna.taylor@homeopathy.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'RECEPTIONIST', 8),
('doctor@clinic.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'DOCTOR', 1),
('reception@clinic.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'RECEPTIONIST', 6);

-- Insert sample patients
INSERT INTO patients (name, date_of_birth, phone_number, email, address, gender, medical_history, emergency_contact, clinic_id) VALUES
('Alice Johnson', '1985-03-15', '+1-555-3001', 'alice.johnson@email.com', '123 Elm Street, City', 'FEMALE', 'Chronic headaches, anxiety', 'Bob Johnson +1-555-3002', 1),
('David Smith', '1978-07-22', '+1-555-3003', 'david.smith@email.com', '456 Maple Avenue, City', 'MALE', 'Digestive issues, insomnia', 'Sarah Smith +1-555-3004', 1),
('Emma Wilson', '1992-11-08', '+1-555-3005', 'emma.wilson@email.com', '789 Oak Road, City', 'FEMALE', 'Allergies, skin conditions', 'Tom Wilson +1-555-3006', 2),
('James Brown', '1965-05-30', '+1-555-3007', 'james.brown@email.com', '321 Pine Street, City', 'MALE', 'Arthritis, high blood pressure', 'Linda Brown +1-555-3008', 2),
('Sophia Davis', '1988-09-12', '+1-555-3009', 'sophia.davis@email.com', '654 Cedar Lane, City', 'FEMALE', 'Migraines, depression', 'Mike Davis +1-555-3010', 3),
('Oliver Taylor', '1995-01-25', '+1-555-3011', 'oliver.taylor@email.com', '987 Birch Avenue, City', 'MALE', 'Asthma, eczema', 'Emma Taylor +1-555-3012', 3);

-- Insert sample appointments
INSERT INTO appointments (patient_id, doctor_id, clinic_id, appointment_date, appointment_time, status, notes) VALUES
(1, 1, 1, CURRENT_DATE, '09:00:00', 'SCHEDULED', 'Follow-up consultation for headaches'),
(2, 2, 1, CURRENT_DATE, '10:30:00', 'SCHEDULED', 'Initial consultation for digestive issues'),
(3, 3, 2, CURRENT_DATE, '14:00:00', 'COMPLETED', 'Allergy treatment review'),
(4, 4, 2, CURRENT_DATE + INTERVAL '1 day', '11:00:00', 'SCHEDULED', 'Arthritis management'),
(5, 5, 3, CURRENT_DATE + INTERVAL '1 day', '15:30:00', 'SCHEDULED', 'Depression counseling'),
(6, 5, 3, CURRENT_DATE + INTERVAL '2 days', '10:00:00', 'SCHEDULED', 'Asthma treatment'),
(1, 1, 1, CURRENT_DATE - INTERVAL '7 days', '09:00:00', 'COMPLETED', 'Previous consultation'),
(2, 2, 1, CURRENT_DATE - INTERVAL '14 days', '10:30:00', 'COMPLETED', 'Initial assessment');

-- Create triggers for updated_at timestamps
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_clinics_updated_at BEFORE UPDATE ON clinics FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_staff_updated_at BEFORE UPDATE ON staff FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_patients_updated_at BEFORE UPDATE ON patients FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_appointments_updated_at BEFORE UPDATE ON appointments FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();