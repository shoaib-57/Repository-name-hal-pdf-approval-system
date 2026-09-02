package com.hal.pdf_approval_system.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hal.pdf_approval_system.dto.ApprovalRequest;
import com.hal.pdf_approval_system.dto.DashboardStatsResponse;
import com.hal.pdf_approval_system.dto.DocumentResponse;
import com.hal.pdf_approval_system.entity.Employee;
import com.hal.pdf_approval_system.service.DocumentService;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/upload")
    public DocumentResponse uploadDocument(
            @RequestParam("file") MultipartFile file,
            Authentication authentication)
            throws IOException {

        Employee employee =
                (Employee) authentication.getPrincipal();

        return documentService.uploadDocument(
                file,
                employee.getId());
    }

    @GetMapping
    public List<DocumentResponse> getAllDocuments(
            Authentication authentication) {

        Employee employee =
                (Employee) authentication.getPrincipal();

        return documentService.getAllDocuments(employee);
    }

    @GetMapping("/{id}")
    public DocumentResponse getDocumentById(
            @PathVariable Long id,
            Authentication authentication) {

        Employee employee =
                (Employee) authentication.getPrincipal();

        return documentService.getDocumentById(
                id,
                employee);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadDocument(
            @PathVariable Long id,
            Authentication authentication)
            throws Exception {

        Employee employee =
                (Employee) authentication.getPrincipal();

        Resource resource =
                documentService.downloadDocument(
                        id,
                        employee);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""
                                + resource.getFilename()
                                + "\"")
                .body(resource);
    }

    /*
     * Only ADMIN or MANAGER can approve.
     *
     * The authenticated user is passed to the service.
     * We no longer trust managerId from the request
     * to identify who performed the approval.
     */
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @PatchMapping("/{id}/approve")
    public DocumentResponse approveDocument(
            @PathVariable Long id,
            @RequestBody ApprovalRequest request,
            Authentication authentication) {

        Employee authenticatedEmployee =
                (Employee) authentication.getPrincipal();

        return documentService.approveDocument(
                id,
                request,
                authenticatedEmployee);
    }

    /*
     * Only ADMIN or MANAGER can reject.
     *
     * The authenticated user is passed to the service.
     */
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @PatchMapping("/{id}/reject")
    public DocumentResponse rejectDocument(
            @PathVariable Long id,
            @RequestBody ApprovalRequest request,
            Authentication authentication) {

        Employee authenticatedEmployee =
                (Employee) authentication.getPrincipal();

        return documentService.rejectDocument(
                id,
                request,
                authenticatedEmployee);
    }

    @GetMapping("/page")
    public Page<DocumentResponse> getDocuments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return documentService.getDocuments(
                page,
                size);
    }

    @GetMapping("/search")
    public List<DocumentResponse> searchByStatus(
            @RequestParam String status) {

        return documentService.searchByStatus(status);
    }

    @GetMapping("/dashboard/stats")
    public DashboardStatsResponse getDashboardStats() {

        return documentService.getDashboardStats();
    }
}