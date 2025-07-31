// Global variables
let currentUser = null;
let authToken = null;
let currentPage = 'dashboard';

// API Base URL
const API_BASE = '/api';

// Initialize app
document.addEventListener('DOMContentLoaded', function() {
    checkAuth();
    initializeEventListeners();
    loadInitialData();
});

// Check authentication
function checkAuth() {
    const token = localStorage.getItem('authToken');
    const user = localStorage.getItem('currentUser');
    
    if (!token || !user) {
        window.location.href = '/login.html';
        return;
    }
    
    authToken = token;
    currentUser = JSON.parse(user);
    
    // Update UI with user info
    document.getElementById('userEmail').textContent = currentUser.email;
    document.getElementById('userRole').textContent = currentUser.role;
}

// Initialize event listeners
function initializeEventListeners() {
    // Navigation
    document.querySelectorAll('.nav-link').forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const page = this.dataset.page;
            showPage(page);
        });
    });

    // Forms
    document.getElementById('patientForm').addEventListener('submit', handlePatientSubmit);
    document.getElementById('appointmentForm').addEventListener('submit', handleAppointmentSubmit);
    document.getElementById('clinicForm').addEventListener('submit', handleClinicSubmit);
    document.getElementById('staffForm').addEventListener('submit', handleStaffSubmit);

    // Filters
    document.getElementById('clinicFilter').addEventListener('change', loadDashboardStats);
    document.getElementById('dateFilter').addEventListener('change', loadDashboardStats);
    document.getElementById('patientSearch').addEventListener('input', debounce(searchPatients, 300));
    document.getElementById('appointmentClinicFilter').addEventListener('change', loadAppointments);
    document.getElementById('appointmentDateFilter').addEventListener('change', loadAppointments);

    // Clinic selection for appointments
    document.getElementById('appointmentClinic').addEventListener('change', loadDoctorsByClinic);
}

// Load initial data
function loadInitialData() {
    loadClinics();
    loadDashboardStats();
    showPage('dashboard');
}

// Show page
function showPage(page) {
    // Update navigation
    document.querySelectorAll('.nav-link').forEach(link => {
        link.classList.remove('active');
    });
    document.querySelector(`[data-page="${page}"]`).classList.add('active');

    // Update content
    document.querySelectorAll('.page').forEach(p => {
        p.classList.remove('active');
    });
    document.getElementById(`${page}-page`).classList.add('active');

    currentPage = page;

    // Load page-specific data
    switch(page) {
        case 'dashboard':
            loadDashboardStats();
            loadRecentAppointments();
            break;
        case 'patients':
            loadPatients();
            break;
        case 'appointments':
            loadAppointments();
            break;
        case 'clinics':
            loadClinicsGrid();
            break;
        case 'staff':
            loadStaff();
            break;
    }
}

// API Helper function
async function apiCall(endpoint, options = {}) {
    const config = {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${authToken}`
        },
        ...options
    };

    try {
        const response = await fetch(`${API_BASE}${endpoint}`, config);
        
        if (response.status === 401) {
            logout();
            return;
        }
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        return await response.json();
    } catch (error) {
        console.error('API call failed:', error);
        showNotification('An error occurred. Please try again.', 'error');
        throw error;
    }
}

// Dashboard functions
async function loadDashboardStats() {
    try {
        const clinicId = document.getElementById('clinicFilter').value;
        const date = document.getElementById('dateFilter').value;
        
        let url = '/dashboard/stats?';
        if (clinicId) url += `clinicId=${clinicId}&`;
        if (date) url += `date=${date}&`;
        
        const stats = await apiCall(url);
        
        document.getElementById('totalPatients').textContent = stats.totalPatients;
        document.getElementById('todayAppointments').textContent = stats.todayAppointments;
        document.getElementById('scheduledAppointments').textContent = stats.scheduledAppointments;
        document.getElementById('completedAppointments').textContent = stats.completedAppointments;
    } catch (error) {
        console.error('Failed to load dashboard stats:', error);
    }
}

async function loadRecentAppointments() {
    try {
        const appointments = await apiCall('/appointments');
        const recentAppointments = appointments.slice(0, 10);
        
        const tbody = document.getElementById('recentAppointmentsTable');
        tbody.innerHTML = recentAppointments.map(appointment => `
            <tr>
                <td>${appointment.patient?.name || 'N/A'}</td>
                <td>${appointment.doctor?.name || 'N/A'}</td>
                <td>${formatDate(appointment.appointmentDate)}</td>
                <td>${appointment.appointmentTime}</td>
                <td><span class="status-badge status-${appointment.status.toLowerCase()}">${appointment.status}</span></td>
            </tr>
        `).join('');
    } catch (error) {
        console.error('Failed to load recent appointments:', error);
    }
}

// Clinic functions
async function loadClinics() {
    try {
        const clinics = await apiCall('/clinics');
        
        // Update clinic filters
        const clinicFilter = document.getElementById('clinicFilter');
        const appointmentClinicFilter = document.getElementById('appointmentClinicFilter');
        const patientClinic = document.getElementById('patientClinic');
        const appointmentClinic = document.getElementById('appointmentClinic');
        
        const clinicOptions = clinics.map(clinic => 
            `<option value="${clinic.id}">${clinic.name}</option>`
        ).join('');
        
        if (clinicFilter) {
            clinicFilter.innerHTML = '<option value="">All Clinics</option>' + clinicOptions;
        }
        if (appointmentClinicFilter) {
            appointmentClinicFilter.innerHTML = '<option value="">All Clinics</option>' + clinicOptions;
        }
        if (patientClinic) {
            patientClinic.innerHTML = '<option value="">Select Clinic</option>' + clinicOptions;
        }
        if (appointmentClinic) {
            appointmentClinic.innerHTML = '<option value="">Select Clinic</option>' + clinicOptions;
        }
        
        // Update staff clinic dropdown
        const staffClinic = document.getElementById('staffClinic');
        if (staffClinic) {
            staffClinic.innerHTML = '<option value="">Select Clinic</option>' + clinicOptions;
        }
    } catch (error) {
        console.error('Failed to load clinics:', error);
    }
}

async function loadClinicsGrid() {
    try {
        const clinics = await apiCall('/clinics');
        
        const grid = document.getElementById('clinicsGrid');
        grid.innerHTML = clinics.map(clinic => `
            <div class="clinic-card">
                <h3><i class="fas fa-hospital"></i> ${clinic.name}</h3>
                <p><i class="fas fa-map-marker-alt"></i> ${clinic.location}</p>
                ${clinic.phoneNumber ? `<p><i class="fas fa-phone"></i> ${clinic.phoneNumber}</p>` : ''}
                ${clinic.email ? `<p><i class="fas fa-envelope"></i> ${clinic.email}</p>` : ''}
                <div class="clinic-actions">
                    <button class="btn btn-primary btn-sm" onclick="editClinic(${clinic.id})">
                        <i class="fas fa-edit"></i> Edit
                    </button>
                    <button class="btn btn-danger btn-sm" onclick="deleteClinic(${clinic.id})">
                        <i class="fas fa-trash"></i> Delete
                    </button>
                </div>
            </div>
        `).join('');
    } catch (error) {
        console.error('Failed to load clinics grid:', error);
    }
}

// Patient functions
async function loadPatients() {
    try {
        const patients = await apiCall('/patients');
        
        const tbody = document.getElementById('patientsTable');
        tbody.innerHTML = patients.map(patient => `
            <tr>
                <td>${patient.name}</td>
                <td>${patient.phoneNumber || 'N/A'}</td>
                <td>${patient.email || 'N/A'}</td>
                <td>${patient.dateOfBirth ? formatDate(patient.dateOfBirth) : 'N/A'}</td>
                <td>${patient.clinic?.name || 'N/A'}</td>
                <td>
                    <button class="action-btn edit" onclick="editPatient(${patient.id})" title="Edit">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="action-btn delete" onclick="deletePatient(${patient.id})" title="Delete">
                        <i class="fas fa-trash"></i>
                    </button>
                </td>
            </tr>
        `).join('');
        
        // Load patients for appointment form
        const appointmentPatient = document.getElementById('appointmentPatient');
        if (appointmentPatient) {
            appointmentPatient.innerHTML = '<option value="">Select Patient</option>' + 
                patients.map(patient => `<option value="${patient.id}">${patient.name}</option>`).join('');
        }
    } catch (error) {
        console.error('Failed to load patients:', error);
    }
}

async function searchPatients() {
    const query = document.getElementById('patientSearch').value;
    if (query.length < 2) {
        loadPatients();
        return;
    }
    
    try {
        const patients = await apiCall(`/patients/search?query=${encodeURIComponent(query)}`);
        
        const tbody = document.getElementById('patientsTable');
        tbody.innerHTML = patients.map(patient => `
            <tr>
                <td>${patient.name}</td>
                <td>${patient.phoneNumber || 'N/A'}</td>
                <td>${patient.email || 'N/A'}</td>
                <td>${patient.dateOfBirth ? formatDate(patient.dateOfBirth) : 'N/A'}</td>
                <td>${patient.clinic?.name || 'N/A'}</td>
                <td>
                    <button class="action-btn edit" onclick="editPatient(${patient.id})" title="Edit">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="action-btn delete" onclick="deletePatient(${patient.id})" title="Delete">
                        <i class="fas fa-trash"></i>
                    </button>
                </td>
            </tr>
        `).join('');
    } catch (error) {
        console.error('Failed to search patients:', error);
    }
}

// Appointment functions
async function loadAppointments() {
    try {
        let url = '/appointments';
        const clinicId = document.getElementById('appointmentClinicFilter').value;
        const date = document.getElementById('appointmentDateFilter').value;
        
        if (clinicId && date) {
            url = `/appointments/clinic/${clinicId}/date/${date}`;
        } else if (clinicId) {
            url = `/appointments/clinic/${clinicId}`;
        } else if (date) {
            url = `/appointments/date/${date}`;
        }
        
        const appointments = await apiCall(url);
        
        const tbody = document.getElementById('appointmentsTable');
        tbody.innerHTML = appointments.map(appointment => `
            <tr>
                <td>${appointment.patient?.name || 'N/A'}</td>
                <td>${appointment.doctor?.name || 'N/A'}</td>
                <td>${appointment.clinic?.name || 'N/A'}</td>
                <td>${formatDate(appointment.appointmentDate)}</td>
                <td>${appointment.appointmentTime}</td>
                <td><span class="status-badge status-${appointment.status.toLowerCase()}">${appointment.status}</span></td>
                <td>
                    <button class="action-btn edit" onclick="editAppointment(${appointment.id})" title="Edit">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="action-btn delete" onclick="deleteAppointment(${appointment.id})" title="Delete">
                        <i class="fas fa-trash"></i>
                    </button>
                    <select onchange="updateAppointmentStatus(${appointment.id}, this.value)" class="form-control" style="width: auto; font-size: 0.75rem;">
                        <option value="SCHEDULED" ${appointment.status === 'SCHEDULED' ? 'selected' : ''}>Scheduled</option>
                        <option value="COMPLETED" ${appointment.status === 'COMPLETED' ? 'selected' : ''}>Completed</option>
                        <option value="CANCELLED" ${appointment.status === 'CANCELLED' ? 'selected' : ''}>Cancelled</option>
                        <option value="NO_SHOW" ${appointment.status === 'NO_SHOW' ? 'selected' : ''}>No Show</option>
                    </select>
                </td>
            </tr>
        `).join('');
    } catch (error) {
        console.error('Failed to load appointments:', error);
    }
}

async function loadDoctorsByClinic() {
    const clinicId = document.getElementById('appointmentClinic').value;
    if (!clinicId) {
        document.getElementById('appointmentDoctor').innerHTML = '<option value="">Select Doctor</option>';
        return;
    }
    
    try {
        const staff = await apiCall(`/staff/clinic/${clinicId}`);
        const doctors = staff.filter(s => s.role === 'DOCTOR');
        
        document.getElementById('appointmentDoctor').innerHTML = '<option value="">Select Doctor</option>' +
            doctors.map(doctor => `<option value="${doctor.id}">${doctor.name}</option>`).join('');
    } catch (error) {
        console.error('Failed to load doctors:', error);
    }
}

// Staff functions
async function loadStaff() {
    try {
        const staff = await apiCall('/staff');
        
        const tbody = document.getElementById('staffTable');
        tbody.innerHTML = staff.map(member => `
            <tr>
                <td>${member.name}</td>
                <td>${member.email}</td>
                <td><span class="status-badge status-${member.role.toLowerCase()}">${member.role}</span></td>
                <td>${member.clinic?.name || 'N/A'}</td>
                <td>${member.phoneNumber || 'N/A'}</td>
                <td>
                    <button class="action-btn edit" onclick="editStaff(${member.id})" title="Edit">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="action-btn delete" onclick="deleteStaff(${member.id})" title="Delete">
                        <i class="fas fa-trash"></i>
                    </button>
                </td>
            </tr>
        `).join('');
    } catch (error) {
        console.error('Failed to load staff:', error);
    }
}

// Staff modal and form functions
function showStaffModal(staffId = null) {
    if (staffId) {
        loadStaffForEdit(staffId);
        document.getElementById('staffModalTitle').textContent = 'Edit Staff';
    } else {
        document.getElementById('staffModalTitle').textContent = 'Add Staff';
    }
    showModal('staffModal');
}

async function editStaff(id) {
    try {
        const staff = await apiCall(`/staff/${id}`);
        
        document.getElementById('staffId').value = staff.id;
        document.getElementById('staffName').value = staff.name;
        document.getElementById('staffEmail').value = staff.email;
        document.getElementById('staffRole').value = staff.role;
        document.getElementById('staffPhone').value = staff.phoneNumber || '';
        document.getElementById('staffSpecialization').value = staff.specialization || '';
        document.getElementById('staffClinic').value = staff.clinic?.id || '';
        
        showStaffModal(id);
    } catch (error) {
        console.error('Failed to load staff for edit:', error);
    }
}

async function deleteStaff(id) {
    if (!confirm('Are you sure you want to delete this staff member?')) return;
    
    try {
        await apiCall(`/staff/${id}`, { method: 'DELETE' });
        showNotification('Staff member deleted successfully!', 'success');
        loadStaff();
    } catch (error) {
        console.error('Failed to delete staff:', error);
    }
}

async function handleStaffSubmit(e) {
    e.preventDefault();
    
    const staffId = document.getElementById('staffId').value;
    
    const staffData = {
        name: document.getElementById('staffName').value,
        email: document.getElementById('staffEmail').value,
        role: document.getElementById('staffRole').value,
        phoneNumber: document.getElementById('staffPhone').value,
        specialization: document.getElementById('staffSpecialization').value,
        clinic: { id: parseInt(document.getElementById('staffClinic').value) }
    };
    
    try {
        if (staffId) {
            await apiCall(`/staff/${staffId}`, {
                method: 'PUT',
                body: JSON.stringify(staffData)
            });
            showNotification('Staff member updated successfully!', 'success');
        } else {
            await apiCall('/staff', {
                method: 'POST',
                body: JSON.stringify(staffData)
            });
            showNotification('Staff member added successfully!', 'success');
        }
        
        closeModal('staffModal');
        loadStaff();
    } catch (error) {
        console.error('Failed to save staff:', error);
    }
}

// Modal functions
function showModal(modalId) {
    document.getElementById(modalId).classList.add('show');
}

function closeModal(modalId) {
    document.getElementById(modalId).classList.remove('show');
    
    // Reset form
    const form = document.querySelector(`#${modalId} form`);
    if (form) {
        form.reset();
        // Clear hidden ID fields
        const idField = form.querySelector('input[type="hidden"]');
        if (idField) idField.value = '';
    }
}

function showPatientModal(patientId = null) {
    if (patientId) {
        loadPatientForEdit(patientId);
        document.getElementById('patientModalTitle').textContent = 'Edit Patient';
    } else {
        document.getElementById('patientModalTitle').textContent = 'Add Patient';
    }
    showModal('patientModal');
}

function showAppointmentModal(appointmentId = null) {
    if (appointmentId) {
        loadAppointmentForEdit(appointmentId);
        document.getElementById('appointmentModalTitle').textContent = 'Edit Appointment';
    } else {
        document.getElementById('appointmentModalTitle').textContent = 'Book Appointment';
    }
    showModal('appointmentModal');
}

function showClinicModal(clinicId = null) {
    if (clinicId) {
        loadClinicForEdit(clinicId);
        document.getElementById('clinicModalTitle').textContent = 'Edit Clinic';
    } else {
        document.getElementById('clinicModalTitle').textContent = 'Add Clinic';
    }
    showModal('clinicModal');
}

// Form handlers
async function handlePatientSubmit(e) {
    e.preventDefault();
    
    const formData = new FormData(e.target);
    const patientId = document.getElementById('patientId').value;
    
    const patientData = {
        name: document.getElementById('patientName').value,
        phoneNumber: document.getElementById('patientPhone').value,
        email: document.getElementById('patientEmail').value,
        dateOfBirth: document.getElementById('patientDob').value || null,
        gender: document.getElementById('patientGender').value || null,
        address: document.getElementById('patientAddress').value,
        medicalHistory: document.getElementById('patientHistory').value,
        emergencyContact: document.getElementById('patientEmergency').value,
        clinic: { id: parseInt(document.getElementById('patientClinic').value) }
    };
    
    try {
        if (patientId) {
            await apiCall(`/patients/${patientId}`, {
                method: 'PUT',
                body: JSON.stringify(patientData)
            });
            showNotification('Patient updated successfully!', 'success');
        } else {
            await apiCall('/patients', {
                method: 'POST',
                body: JSON.stringify(patientData)
            });
            showNotification('Patient added successfully!', 'success');
        }
        
        closeModal('patientModal');
        loadPatients();
    } catch (error) {
        console.error('Failed to save patient:', error);
    }
}

async function handleAppointmentSubmit(e) {
    e.preventDefault();
    
    const appointmentId = document.getElementById('appointmentId').value;
    
    const appointmentData = {
        patient: { id: parseInt(document.getElementById('appointmentPatient').value) },
        doctor: { id: parseInt(document.getElementById('appointmentDoctor').value) },
        clinic: { id: parseInt(document.getElementById('appointmentClinic').value) },
        appointmentDate: document.getElementById('appointmentDate').value,
        appointmentTime: document.getElementById('appointmentTime').value,
        status: document.getElementById('appointmentStatus').value,
        notes: document.getElementById('appointmentNotes').value
    };
    
    try {
        if (appointmentId) {
            await apiCall(`/appointments/${appointmentId}`, {
                method: 'PUT',
                body: JSON.stringify(appointmentData)
            });
            showNotification('Appointment updated successfully!', 'success');
        } else {
            await apiCall('/appointments', {
                method: 'POST',
                body: JSON.stringify(appointmentData)
            });
            showNotification('Appointment booked successfully!', 'success');
        }
        
        closeModal('appointmentModal');
        loadAppointments();
        if (currentPage === 'dashboard') {
            loadDashboardStats();
            loadRecentAppointments();
        }
    } catch (error) {
        console.error('Failed to save appointment:', error);
    }
}

async function handleClinicSubmit(e) {
    e.preventDefault();
    
    const clinicId = document.getElementById('clinicId').value;
    
    const clinicData = {
        name: document.getElementById('clinicName').value,
        location: document.getElementById('clinicLocation').value,
        phoneNumber: document.getElementById('clinicPhone').value,
        email: document.getElementById('clinicEmail').value
    };
    
    try {
        if (clinicId) {
            await apiCall(`/clinics/${clinicId}`, {
                method: 'PUT',
                body: JSON.stringify(clinicData)
            });
            showNotification('Clinic updated successfully!', 'success');
        } else {
            await apiCall('/clinics', {
                method: 'POST',
                body: JSON.stringify(clinicData)
            });
            showNotification('Clinic added successfully!', 'success');
        }
        
        closeModal('clinicModal');
        loadClinicsGrid();
        loadClinics(); // Refresh dropdowns
    } catch (error) {
        console.error('Failed to save clinic:', error);
    }
}

// Edit functions
async function editPatient(id) {
    try {
        const patient = await apiCall(`/patients/${id}`);
        
        document.getElementById('patientId').value = patient.id;
        document.getElementById('patientName').value = patient.name;
        document.getElementById('patientPhone').value = patient.phoneNumber || '';
        document.getElementById('patientEmail').value = patient.email || '';
        document.getElementById('patientDob').value = patient.dateOfBirth || '';
        document.getElementById('patientGender').value = patient.gender || '';
        document.getElementById('patientAddress').value = patient.address || '';
        document.getElementById('patientHistory').value = patient.medicalHistory || '';
        document.getElementById('patientEmergency').value = patient.emergencyContact || '';
        document.getElementById('patientClinic').value = patient.clinic?.id || '';
        
        showPatientModal(id);
    } catch (error) {
        console.error('Failed to load patient for edit:', error);
    }
}

async function editAppointment(id) {
    try {
        const appointment = await apiCall(`/appointments/${id}`);
        
        document.getElementById('appointmentId').value = appointment.id;
        document.getElementById('appointmentPatient').value = appointment.patient?.id || '';
        document.getElementById('appointmentClinic').value = appointment.clinic?.id || '';
        document.getElementById('appointmentDoctor').value = appointment.doctor?.id || '';
        document.getElementById('appointmentDate').value = appointment.appointmentDate;
        document.getElementById('appointmentTime').value = appointment.appointmentTime;
        document.getElementById('appointmentStatus').value = appointment.status;
        document.getElementById('appointmentNotes').value = appointment.notes || '';
        
        // Load doctors for the selected clinic
        await loadDoctorsByClinic();
        document.getElementById('appointmentDoctor').value = appointment.doctor?.id || '';
        
        showAppointmentModal(id);
    } catch (error) {
        console.error('Failed to load appointment for edit:', error);
    }
}

async function editClinic(id) {
    try {
        const clinic = await apiCall(`/clinics/${id}`);
        
        document.getElementById('clinicId').value = clinic.id;
        document.getElementById('clinicName').value = clinic.name;
        document.getElementById('clinicLocation').value = clinic.location;
        document.getElementById('clinicPhone').value = clinic.phoneNumber || '';
        document.getElementById('clinicEmail').value = clinic.email || '';
        
        showClinicModal(id);
    } catch (error) {
        console.error('Failed to load clinic for edit:', error);
    }
}

// Delete functions
async function deletePatient(id) {
    if (!confirm('Are you sure you want to delete this patient?')) return;
    
    try {
        await apiCall(`/patients/${id}`, { method: 'DELETE' });
        showNotification('Patient deleted successfully!', 'success');
        loadPatients();
    } catch (error) {
        console.error('Failed to delete patient:', error);
    }
}

async function deleteAppointment(id) {
    if (!confirm('Are you sure you want to delete this appointment?')) return;
    
    try {
        await apiCall(`/appointments/${id}`, { method: 'DELETE' });
        showNotification('Appointment deleted successfully!', 'success');
        loadAppointments();
        if (currentPage === 'dashboard') {
            loadDashboardStats();
            loadRecentAppointments();
        }
    } catch (error) {
        console.error('Failed to delete appointment:', error);
    }
}

async function deleteClinic(id) {
    if (!confirm('Are you sure you want to delete this clinic?')) return;
    
    try {
        await apiCall(`/clinics/${id}`, { method: 'DELETE' });
        showNotification('Clinic deleted successfully!', 'success');
        loadClinicsGrid();
        loadClinics(); // Refresh dropdowns
    } catch (error) {
        console.error('Failed to delete clinic:', error);
    }
}

// Update appointment status
async function updateAppointmentStatus(id, status) {
    try {
        await apiCall(`/appointments/${id}/status?status=${status}`, { method: 'PATCH' });
        showNotification('Appointment status updated!', 'success');
        if (currentPage === 'dashboard') {
            loadDashboardStats();
            loadRecentAppointments();
        }
    } catch (error) {
        console.error('Failed to update appointment status:', error);
    }
}

// Utility functions
function formatDate(dateString) {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleDateString();
}

function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

function showNotification(message, type = 'info') {
    // Create notification element
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.innerHTML = `
        <i class="fas fa-${type === 'success' ? 'check-circle' : type === 'error' ? 'exclamation-circle' : 'info-circle'}"></i>
        <span>${message}</span>
        <button onclick="this.parentElement.remove()" class="notification-close">
            <i class="fas fa-times"></i>
        </button>
    `;
    
    // Add to page
    document.body.appendChild(notification);
    
    // Auto remove after 5 seconds
    setTimeout(() => {
        if (notification.parentElement) {
            notification.remove();
        }
    }, 5000);
}

// Sidebar toggle
function toggleSidebar() {
    document.getElementById('sidebar').classList.toggle('show');
}

// Logout
function logout() {
    localStorage.removeItem('authToken');
    localStorage.removeItem('currentUser');
    window.location.href = '/login.html';
}

// Close modal when clicking outside
document.addEventListener('click', function(e) {
    if (e.target.classList.contains('modal')) {
        e.target.classList.remove('show');
    }
});

// Add notification styles
const notificationStyles = `
.notification {
    position: fixed;
    top: 20px;
    right: 20px;
    background: white;
    padding: 1rem 1.5rem;
    border-radius: 8px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
    display: flex;
    align-items: center;
    gap: 0.75rem;
    z-index: 3000;
    min-width: 300px;
    animation: slideInRight 0.3s ease;
}

.notification-success {
    border-left: 4px solid #28a745;
    color: #155724;
}

.notification-error {
    border-left: 4px solid #dc3545;
    color: #721c24;
}

.notification-info {
    border-left: 4px solid #17a2b8;
    color: #0c5460;
}

.notification-close {
    background: none;
    border: none;
    cursor: pointer;
    color: #666;
    margin-left: auto;
}

@keyframes slideInRight {
    from {
        transform: translateX(100%);
        opacity: 0;
    }
    to {
        transform: translateX(0);
        opacity: 1;
    }
}
`;

// Add styles to head
const styleSheet = document.createElement('style');
styleSheet.textContent = notificationStyles;
document.head.appendChild(styleSheet);