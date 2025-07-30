# Homeopathic Clinic Management System

A comprehensive web application for managing homeopathic clinics with Spring Boot backend and responsive HTML/CSS/JavaScript frontend.

## 🏥 Features

### Core Functionality
- **Multi-Clinic Support**: Manage multiple clinic locations with separate staff and patients
- **Patient Management**: Complete patient profiles with medical history and contact information
- **Appointment Scheduling**: Book, manage, and track appointments across clinics
- **Staff Management**: Role-based access for Admins, Doctors, and Receptionists
- **Dashboard Analytics**: Real-time statistics and appointment summaries

### Security
- JWT-based authentication
- Role-based access control
- Secure password hashing with BCrypt
- CORS configuration for cross-origin requests

### User Interface
- Responsive design for desktop and mobile
- Clean, modern interface with intuitive navigation
- Real-time search and filtering
- Modal-based forms for data entry
- Status badges and visual indicators

## 🛠 Technology Stack

### Backend
- **Spring Boot 3.2.0** - Main framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database operations
- **PostgreSQL** - Primary database
- **JWT** - Token-based authentication
- **Maven** - Dependency management

### Frontend
- **HTML5** - Structure
- **CSS3** - Styling with modern features
- **JavaScript (ES6+)** - Interactive functionality
- **Font Awesome** - Icons
- **Responsive Design** - Mobile-first approach

## 📋 Prerequisites

- Java 17 or higher
- PostgreSQL 12 or higher
- Maven 3.6 or higher
- Modern web browser

## 🚀 Installation & Setup

### 1. Database Setup

```sql
-- Create database
CREATE DATABASE homeopathy_clinic;

-- Create user (optional)
CREATE USER clinic_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE homeopathy_clinic TO clinic_user;
```

### 2. Application Configuration

Update `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/homeopathy_clinic
    username: postgres  # or your database user
    password: password  # your database password
```

### 3. Build and Run

```bash
# Clone the repository
git clone <repository-url>
cd clinic-management

# Build the application
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 4. Database Initialization

The application will automatically create tables and insert sample data on first run.

## 👥 Default Users

| Role | Email | Password |
|------|-------|----------|
| Admin | admin@clinic.com | admin123 |
| Doctor | doctor@clinic.com | doctor123 |
| Receptionist | reception@clinic.com | reception123 |

## 📊 Database Schema

### Core Tables
- **clinics** - Clinic locations and information
- **staff** - Staff members with roles and clinic assignments
- **patients** - Patient profiles and medical history
- **appointments** - Appointment scheduling and status
- **users** - Authentication and user management

### Key Relationships
- Staff belongs to Clinics (Many-to-One)
- Patients belongs to Clinics (Many-to-One)
- Appointments link Patients, Doctors, and Clinics
- Users link to Staff for authentication

## 🔧 API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout

### Clinics
- `GET /api/clinics` - List all clinics
- `POST /api/clinics` - Create new clinic (Admin only)
- `PUT /api/clinics/{id}` - Update clinic (Admin only)
- `DELETE /api/clinics/{id}` - Delete clinic (Admin only)

### Patients
- `GET /api/patients` - List all patients
- `POST /api/patients` - Create new patient
- `PUT /api/patients/{id}` - Update patient
- `DELETE /api/patients/{id}` - Delete patient
- `GET /api/patients/search?query={term}` - Search patients

### Appointments
- `GET /api/appointments` - List all appointments
- `POST /api/appointments` - Create new appointment
- `PUT /api/appointments/{id}` - Update appointment
- `DELETE /api/appointments/{id}` - Delete appointment
- `PATCH /api/appointments/{id}/status` - Update appointment status

### Dashboard
- `GET /api/dashboard/stats` - Get dashboard statistics

## 🎨 Frontend Structure

```
src/main/resources/static/
├── index.html          # Main application
├── login.html          # Login page
├── css/
│   ├── style.css       # Main application styles
│   └── login.css       # Login page styles
└── js/
    ├── app.js          # Main application logic
    └── login.js        # Login functionality
```

## 🔐 Security Features

- **JWT Authentication**: Secure token-based authentication
- **Role-Based Access**: Different permissions for Admin, Doctor, Receptionist
- **Password Hashing**: BCrypt encryption for passwords
- **CORS Protection**: Configured for secure cross-origin requests
- **Input Validation**: Server-side validation for all inputs

## 📱 Responsive Design

The application is fully responsive and works on:
- Desktop computers (1200px+)
- Tablets (768px - 1199px)
- Mobile phones (320px - 767px)

## 🚀 Deployment

### Production Configuration

1. Update database configuration for production
2. Set secure JWT secret key
3. Configure HTTPS
4. Set up proper CORS origins
5. Enable production logging

### Docker Deployment (Optional)

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/clinic-management-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Check the documentation
- Review the API endpoints

## 🔄 Future Enhancements

- Email notifications for appointments
- SMS reminders
- Report generation (PDF/Excel)
- Prescription management
- Billing and payment integration
- Mobile app development
- Advanced analytics and reporting