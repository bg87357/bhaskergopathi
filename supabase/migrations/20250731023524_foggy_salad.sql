-- Insert sample clinics
INSERT INTO clinics (name, location, phone_number, email, created_at, updated_at) VALUES
('HomeoClinic Central', '123 Main Street, Downtown, City Center', '+1-555-0101', 'central@homeoclinic.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('HomeoClinic North', '456 Oak Avenue, North District, Suburb Area', '+1-555-0102', 'north@homeoclinic.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('HomeoClinic South', '789 Pine Road, South Valley, Residential Zone', '+1-555-0103', 'south@homeoclinic.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample staff
INSERT INTO staff (name, email, role, phone_number, specialization, clinic_id, created_at, updated_at) VALUES
-- Central Clinic Staff
('Dr. Sarah Johnson', 'sarah.johnson@homeoclinic.com', 'DOCTOR', '+1-555-1001', 'General Homeopathy, Chronic Diseases', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Dr. Michael Chen', 'michael.chen@homeoclinic.com', 'DOCTOR', '+1-555-1002', 'Pediatric Homeopathy, Allergies', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Emily Davis', 'emily.davis@homeoclinic.com', 'RECEPTIONIST', '+1-555-1003', NULL, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Admin User', 'admin@clinic.com', 'ADMIN', '+1-555-1000', 'System Administration', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- North Clinic Staff
('Dr. Robert Wilson', 'robert.wilson@homeoclinic.com', 'DOCTOR', '+1-555-2001', 'Mental Health, Stress Management', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Dr. Lisa Anderson', 'lisa.anderson@homeoclinic.com', 'DOCTOR', '+1-555-2002', 'Women Health, Hormonal Issues', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('James Miller', 'james.miller@homeoclinic.com', 'RECEPTIONIST', '+1-555-2003', NULL, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- South Clinic Staff
('Dr. Maria Garcia', 'maria.garcia@homeoclinic.com', 'DOCTOR', '+1-555-3001', 'Digestive Disorders, Nutrition', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Dr. David Brown', 'david.brown@homeoclinic.com', 'DOCTOR', '+1-555-3002', 'Respiratory Issues, Immunity', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Anna Thompson', 'anna.thompson@homeoclinic.com', 'RECEPTIONIST', '+1-555-3003', NULL, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample users (passwords are hashed for: admin123, doctor123, reception123)
INSERT INTO users (email, password, role, staff_id, created_at, updated_at) VALUES
('admin@clinic.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'ADMIN', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('doctor@clinic.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'DOCTOR', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('reception@clinic.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'RECEPTIONIST', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sarah.johnson@homeoclinic.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'DOCTOR', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('michael.chen@homeoclinic.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'DOCTOR', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('robert.wilson@homeoclinic.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'DOCTOR', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('lisa.anderson@homeoclinic.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'DOCTOR', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('maria.garcia@homeoclinic.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'DOCTOR', 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('david.brown@homeoclinic.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'DOCTOR', 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample patients
INSERT INTO patients (name, date_of_birth, phone_number, email, address, gender, medical_history, emergency_contact, clinic_id, created_at, updated_at) VALUES
-- Central Clinic Patients
('John Smith', '1985-03-15', '+1-555-4001', 'john.smith@email.com', '123 Elm Street, City Center', 'MALE', 'Chronic headaches, stress-related symptoms. Previous treatments with conventional medicine showed limited success.', 'Jane Smith - +1-555-4002', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Mary Johnson', '1978-07-22', '+1-555-4003', 'mary.johnson@email.com', '456 Maple Avenue, Downtown', 'FEMALE', 'Digestive issues, IBS symptoms. Seeking natural treatment alternatives.', 'Robert Johnson - +1-555-4004', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Peter Wilson', '1992-11-08', '+1-555-4005', 'peter.wilson@email.com', '789 Cedar Lane, City Center', 'MALE', 'Anxiety, sleep disorders. Looking for holistic approach to mental health.', 'Sarah Wilson - +1-555-4006', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Lisa Davis', '1988-05-30', '+1-555-4007', 'lisa.davis@email.com', '321 Birch Road, Downtown', 'FEMALE', 'Hormonal imbalances, irregular menstrual cycles. Prefers natural remedies.', 'Mike Davis - +1-555-4008', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- North Clinic Patients
('Robert Brown', '1975-12-03', '+1-555-5001', 'robert.brown@email.com', '654 Spruce Street, North District', 'MALE', 'High blood pressure, cardiovascular concerns. Family history of heart disease.', 'Linda Brown - +1-555-5002', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Jennifer Lee', '1983-09-17', '+1-555-5003', 'jennifer.lee@email.com', '987 Willow Drive, Suburb Area', 'FEMALE', 'Chronic fatigue syndrome, low energy levels. Multiple food sensitivities.', 'David Lee - +1-555-5004', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Michael Taylor', '1990-01-25', '+1-555-5005', 'michael.taylor@email.com', '147 Poplar Avenue, North District', 'MALE', 'Skin conditions, eczema. Allergic reactions to conventional treatments.', 'Emma Taylor - +1-555-5006', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- South Clinic Patients
('Sarah Martinez', '1986-04-12', '+1-555-6001', 'sarah.martinez@email.com', '258 Ash Boulevard, South Valley', 'FEMALE', 'Respiratory issues, asthma. Seeking complementary treatment options.', 'Carlos Martinez - +1-555-6002', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('David Anderson', '1979-08-07', '+1-555-6003', 'david.anderson@email.com', '369 Oak Terrace, Residential Zone', 'MALE', 'Joint pain, arthritis symptoms. Previous surgeries with limited improvement.', 'Helen Anderson - +1-555-6004', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Amanda White', '1994-06-19', '+1-555-6005', 'amanda.white@email.com', '741 Pine Circle, South Valley', 'FEMALE', 'Depression, mood swings. Looking for natural mood stabilizers.', 'Tom White - +1-555-6006', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample appointments
INSERT INTO appointments (patient_id, doctor_id, clinic_id, appointment_date, appointment_time, status, notes, prescription, created_at, updated_at) VALUES
-- Today's appointments
(1, 1, 1, CURRENT_DATE, '09:00:00', 'SCHEDULED', 'Initial consultation for chronic headaches', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2, 1, CURRENT_DATE, '10:30:00', 'SCHEDULED', 'Follow-up for digestive issues', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 5, 2, CURRENT_DATE, '11:00:00', 'COMPLETED', 'Blood pressure monitoring', 'Crataegus 30C, twice daily', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 8, 3, CURRENT_DATE, '14:00:00', 'SCHEDULED', 'Respiratory assessment', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Tomorrow's appointments
(3, 1, 1, CURRENT_DATE + INTERVAL '1 day', '09:30:00', 'SCHEDULED', 'Anxiety treatment consultation', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 6, 2, CURRENT_DATE + INTERVAL '1 day', '10:00:00', 'SCHEDULED', 'Chronic fatigue evaluation', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 9, 3, CURRENT_DATE + INTERVAL '1 day', '15:30:00', 'SCHEDULED', 'Joint pain assessment', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Past appointments
(4, 2, 1, CURRENT_DATE - INTERVAL '1 day', '11:30:00', 'COMPLETED', 'Hormonal balance consultation', 'Sepia 200C, once weekly', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 5, 2, CURRENT_DATE - INTERVAL '2 days', '14:30:00', 'COMPLETED', 'Skin condition treatment', 'Sulphur 30C, daily', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 8, 3, CURRENT_DATE - INTERVAL '3 days', '16:00:00', 'CANCELLED', 'Patient cancelled due to illness', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Future appointments
(1, 1, 1, CURRENT_DATE + INTERVAL '7 days', '09:00:00', 'SCHEDULED', 'Follow-up for headache treatment', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2, 1, CURRENT_DATE + INTERVAL '14 days', '10:30:00', 'SCHEDULED', 'Progress review for digestive health', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);