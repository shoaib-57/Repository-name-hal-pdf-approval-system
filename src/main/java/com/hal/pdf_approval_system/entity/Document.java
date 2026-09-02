package com.hal.pdf_approval_system.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "DOCUMENT")
@Getter
@Setter
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "document_seq_generator")
    @SequenceGenerator(name = "document_seq_generator", sequenceName = "DOCUMENT_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "DOCUMENT_NAME")
    private String documentName;

    @Column(name = "FILE_PATH")
    private String filePath;

    @Column(name = "STATUS")
    private String status;

    @ManyToOne
    @JoinColumn(name = "UPLOADED_BY")
    private Employee uploadedBy;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "APPROVED_BY")
    private Employee approvedBy;

    @Column(name = "APPROVED_AT")
    private LocalDateTime approvedAt;

    @Column(name = "COMMENTS")
    private String comments;
}