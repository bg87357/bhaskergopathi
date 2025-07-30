package com.homeopathy.service;

import com.homeopathy.dto.LoginRequest;
import com.homeopathy.dto.LoginResponse;
import com.homeopathy.entity.User;
import com.homeopathy.repository.UserRepository;
import com.homeopathy.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        String token = jwtTokenProvider.generateToken(authentication);
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        LoginResponse response = new LoginResponse(token, user.getEmail(), user.getRole());
        
        if (user.getStaff() != null) {
            response.setStaffId(user.getStaff().getId());
            response.setStaffName(user.getStaff().getName());
            response.setClinicId(user.getStaff().getClinic().getId());
        }

        return response;
    }
}