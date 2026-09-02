package com.hal.pdf_approval_system.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentResponse {

    private Long id;

    private String departmentName;

    private LocalDateTime createdAt;
}