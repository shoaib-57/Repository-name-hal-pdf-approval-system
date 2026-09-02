package com.hal.pdf_approval_system.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hal.pdf_approval_system.dto.DepartmentRequest;
import com.hal.pdf_approval_system.dto.DepartmentResponse;
import com.hal.pdf_approval_system.entity.Department;
import com.hal.pdf_approval_system.repository.DepartmentRepository;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    // CREATE DEPARTMENT
    public DepartmentResponse createDepartment(DepartmentRequest request) {

        Department department = new Department();

        department.setDepartmentName(request.getDepartmentName());
        department.setCreatedAt(LocalDateTime.now());

        departmentRepository.save(department);

        return mapToResponse(department);
    }

    // GET ALL DEPARTMENTS
    public List<DepartmentResponse> getAllDepartments() {

        return departmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // GET DEPARTMENT BY ID
    public DepartmentResponse getDepartmentById(Long id) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Department not found with id: " + id));

        return mapToResponse(department);
    }

    // UPDATE DEPARTMENT
    public DepartmentResponse updateDepartment(
            Long id,
            DepartmentRequest request) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Department not found with id: " + id));

        department.setDepartmentName(request.getDepartmentName());

        departmentRepository.save(department);

        return mapToResponse(department);
    }

    // DELETE DEPARTMENT
    public void deleteDepartment(Long id) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Department not found with id: " + id));

        departmentRepository.delete(department);
    }

    // COMMON MAPPER
    private DepartmentResponse mapToResponse(Department department) {

        DepartmentResponse response = new DepartmentResponse();

        response.setId(department.getId());
        response.setDepartmentName(department.getDepartmentName());
        response.setCreatedAt(department.getCreatedAt());

        return response;
    }
}