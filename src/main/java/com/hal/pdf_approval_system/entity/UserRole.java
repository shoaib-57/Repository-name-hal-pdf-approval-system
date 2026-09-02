package com.hal.pdf_approval_system.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "USER_ROLE")
@Getter
@Setter
public class UserRole {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_role_seq_generator"
    )
    @SequenceGenerator(
            name = "user_role_seq_generator",
            sequenceName = "USER_ROLE_SEQ",
            allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;

    @Column(name = "ROLE_NAME")
    private String roleName;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
}
