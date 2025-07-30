package com.homeopathy.dto;

import com.homeopathy.entity.User;

public class LoginResponse {
    private String token;
    private String email;
    private User.Role role;
    private Long staffId;
    private String staffName;
    private Long clinicId;

    // Constructors
    public LoginResponse() {}

    public LoginResponse(String token, String email, User.Role role) {
        this.token = token;
        this.email = email;
        this.role = role;
    }

    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public User.Role getRole() { return role; }
    public void setRole(User.Role role) { this.role = role; }

    public Long getStaffId() { return staffId; }
    public void setStaffId(Long staffId) { this.staffId = staffId; }

    public String getStaffName() { return staffName; }
    public void setStaffName(String staffName) { this.staffName = staffName; }

    public Long getClinicId() { return clinicId; }
    public void setClinicId(Long clinicId) { this.clinicId = clinicId; }
}