package com.hal.pdf_approval_system.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeResponse {

    private Long id;

    private String employeeId;

    private String firstName;

    private String lastName;

    private String email;

    private String designation;

    private String department;

    private String role;

    private Integer active;
}