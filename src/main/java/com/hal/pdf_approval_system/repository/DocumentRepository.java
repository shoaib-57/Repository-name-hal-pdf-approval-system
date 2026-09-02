package com.hal.pdf_approval_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hal.pdf_approval_system.entity.Document;

public interface DocumentRepository
        extends JpaRepository<Document, Long> {

    List<Document> findByStatus(String status);

    long countByStatus(String status);
}