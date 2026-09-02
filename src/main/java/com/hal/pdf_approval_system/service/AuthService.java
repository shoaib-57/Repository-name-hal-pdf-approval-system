package com.hal.pdf_approval_system.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hal.pdf_approval_system.dto.LoginRequest;
import com.hal.pdf_approval_system.dto.LoginResponse;
import com.hal.pdf_approval_system.entity.Employee;
import com.hal.pdf_approval_system.exception.InvalidCredentialsException;
import com.hal.pdf_approval_system.repository.EmployeeRepository;
import com.hal.pdf_approval_system.security.JwtService;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            EmployeeRepository employeeRepository,
            BCryptPasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {

        Employee employee = employeeRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                employee.getPassword())) {

            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtService.generateToken(employee);

        logger.info("Login successful for {}", employee.getEmail());

        return new LoginResponse(
                employee.getId(),
                employee.getEmployeeId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getRole().getRoleName(),
                "Login Successful",
                token);
    }
}