package com.hal.pdf_approval_system.filter;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hal.pdf_approval_system.entity.Employee;
import com.hal.pdf_approval_system.repository.EmployeeRepository;
import com.hal.pdf_approval_system.security.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtService jwtService;
    private final EmployeeRepository employeeRepository;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            EmployeeRepository employeeRepository) {

        this.jwtService = jwtService;
        this.employeeRepository = employeeRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);

        logger.debug("JWT received");

        String username = jwtService.extractUsername(jwt);

        Employee employee = employeeRepository.findByEmail(username)
                .orElse(null);

        if (employee != null && jwtService.isTokenValid(jwt, employee)) {

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    employee,
                    null,
                    List.of(
                            new SimpleGrantedAuthority(
                                    "ROLE_" + employee.getRole().getRoleName())));

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

       if (employee != null) {
    logger.info("Authenticated user: {}", employee.getEmail());
    } else {
    logger.warn("No employee found for JWT username: {}", username);
    }

        logger.debug("Username extracted: {}", username);

        filterChain.doFilter(request, response);
    }
}