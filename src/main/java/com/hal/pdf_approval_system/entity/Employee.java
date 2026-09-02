package com.hal.pdf_approval_system.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "EMPLOYEE")
@Getter
@Setter
public class Employee {

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_seq_generator")
        @SequenceGenerator(name = "employee_seq_generator", sequenceName = "EMPLOYEE_SEQ", allocationSize = 1)
        @Column(name = "ID")
        private Long id;

        @Column(name = "EMPLOYEE_ID", nullable = false, unique = true)
        private String employeeId;

        @Column(name = "FIRST_NAME", nullable = false)
        private String firstName;

        @Column(name = "LAST_NAME", nullable = false)
        private String lastName;

        @Column(name = "EMAIL", nullable = false, unique = true)
        private String email;

        @Column(name = "PASSWORD", nullable = false)
        private String password;

        @Column(name = "DESIGNATION", nullable = false)
        private String designation;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "DEPARTMENT_ID", nullable = false)
        private Department department;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "ROLE_ID", nullable = false)
        private UserRole role;

        @Column(name = "ACTIVE")
        private Integer active;

        @Column(name = "CREATED_AT")
        private LocalDateTime createdAt;
}