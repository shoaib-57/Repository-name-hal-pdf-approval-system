package com.hal.pdf_approval_system.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "DEPARTMENT")
@Getter
@Setter
public class Department {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "department_seq_generator"
    )
    @SequenceGenerator(
            name = "department_seq_generator",
            sequenceName = "DEPARTMENT_SEQ",
            allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;

    @Column(name = "DEPARTMENT_NAME", nullable = false, unique = true)
    private String departmentName;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
}