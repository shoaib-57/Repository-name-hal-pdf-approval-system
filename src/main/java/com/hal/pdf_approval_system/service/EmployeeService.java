package com.hal.pdf_approval_system.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hal.pdf_approval_system.dto.EmployeeRequest;
import com.hal.pdf_approval_system.dto.EmployeeResponse;
import com.hal.pdf_approval_system.entity.Department;
import com.hal.pdf_approval_system.entity.Employee;
import com.hal.pdf_approval_system.entity.UserRole;
import com.hal.pdf_approval_system.exception.EmployeeNotFoundException;
import com.hal.pdf_approval_system.repository.DepartmentRepository;
import com.hal.pdf_approval_system.repository.EmployeeRepository;
import com.hal.pdf_approval_system.repository.UserRoleRepository;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRoleRepository userRoleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public EmployeeService(
            EmployeeRepository employeeRepository,
            DepartmentRepository departmentRepository,
            UserRoleRepository userRoleRepository,
            BCryptPasswordEncoder passwordEncoder) {

        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // CREATE
    public EmployeeResponse createEmployee(EmployeeRequest request) {

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        UserRole role = userRoleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Employee employee = new Employee();

        employee.setEmployeeId(request.getEmployeeId());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPassword(passwordEncoder.encode(request.getPassword()));
        employee.setDesignation(request.getDesignation());
        employee.setDepartment(department);
        employee.setRole(role);
        employee.setActive(1);
        employee.setCreatedAt(java.time.LocalDateTime.now());

        employeeRepository.save(employee);

        return mapToResponse(employee);
    }

    // GET ALL
    public List<EmployeeResponse> getAllEmployees() {

        return employeeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // GET BY ID
    public EmployeeResponse getEmployeeById(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee not found with id: " + id));

        return mapToResponse(employee);
    }

    // UPDATE
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee not found with id: " + id));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        UserRole role = userRoleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        employee.setEmployeeId(request.getEmployeeId());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setDesignation(request.getDesignation());
        employee.setDepartment(department);
        employee.setRole(role);

        // Change password only if a new password was provided
        if (request.getPassword() != null &&
                !request.getPassword().isBlank()) {

            employee.setPassword(
                    passwordEncoder.encode(request.getPassword()));
        }

        employeeRepository.save(employee);

        return mapToResponse(employee);
    }

    // DELETE
    public void deleteEmployee(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee not found with id: " + id));

        employeeRepository.delete(employee);
    }

    // COMMON MAPPER
    private EmployeeResponse mapToResponse(Employee employee) {

        EmployeeResponse response = new EmployeeResponse();

        response.setId(employee.getId());
        response.setEmployeeId(employee.getEmployeeId());
        response.setFirstName(employee.getFirstName());
        response.setLastName(employee.getLastName());
        response.setEmail(employee.getEmail());
        response.setDesignation(employee.getDesignation());
        response.setDepartment(
                employee.getDepartment().getDepartmentName());
        response.setRole(
                employee.getRole().getRoleName());
        response.setActive(employee.getActive());

        return response;
    }
}