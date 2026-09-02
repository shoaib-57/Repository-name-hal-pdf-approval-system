package com.hal.pdf_approval_system.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentResponse {

    private Long id;

    private String documentName;

    private String status;

    private String uploadedBy;

    private LocalDateTime createdAt;
}