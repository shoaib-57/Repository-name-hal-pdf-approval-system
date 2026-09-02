package com.hal.pdf_approval_system.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hal.pdf_approval_system.dto.ApprovalRequest;
import com.hal.pdf_approval_system.dto.DashboardStatsResponse;
import com.hal.pdf_approval_system.dto.DocumentResponse;
import com.hal.pdf_approval_system.entity.Document;
import com.hal.pdf_approval_system.entity.Employee;
import com.hal.pdf_approval_system.exception.DocumentNotFoundException;
import com.hal.pdf_approval_system.exception.EmployeeNotFoundException;
import com.hal.pdf_approval_system.repository.DocumentRepository;

@Service
public class DocumentService {

    private final QrPdfService qrPdfService;
    private final com.hal.pdf_approval_system.repository.DocumentRepository documentRepository;
    private final com.hal.pdf_approval_system.repository.EmployeeRepository employeeRepository;

    public DocumentService(
            com.hal.pdf_approval_system.repository.DocumentRepository documentRepository,
            com.hal.pdf_approval_system.repository.EmployeeRepository employeeRepository,
            QrPdfService qrPdfService) {

        this.documentRepository = documentRepository;
        this.employeeRepository = employeeRepository;
        this.qrPdfService = qrPdfService;
    }

    /*
     * ==============================
     * UPLOAD DOCUMENT
     * ==============================
     */
    public DocumentResponse uploadDocument(
            MultipartFile file,
            Long employeeId)
            throws IOException {

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new EmployeeNotFoundException(
                                        "Employee not found"));

        Document document =
                new Document();

        String uploadDir =
                "uploads";

        Path uploadPath =
                Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFileName =
                file.getOriginalFilename();

        if (originalFileName == null ||
                originalFileName.isBlank()) {

            throw new IOException(
                    "Invalid file name");
        }

        String fileName =
                UUID.randomUUID()
                        + "_"
                        + originalFileName;

        Path filePath =
                uploadPath.resolve(fileName);

        Files.copy(
                file.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING);

        document.setDocumentName(
                originalFileName);

        document.setFilePath(
                filePath.toString());

        document.setStatus(
                "PENDING");

        document.setUploadedBy(
                employee);

        document.setCreatedAt(
                LocalDateTime.now());

        documentRepository.save(
                document);

        return buildResponse(
                document);
    }

    /*
     * ==============================
     * GET ALL DOCUMENTS
     * ==============================
     */
    public List<DocumentResponse> getAllDocuments(
            Employee authenticatedEmployee) {

        boolean isAdminOrManager =
                authenticatedEmployee
                        .getRole()
                        .getRoleName()
                        .equals("ADMIN")
                        ||
                authenticatedEmployee
                        .getRole()
                        .getRoleName()
                        .equals("MANAGER");

        if (isAdminOrManager) {

            return documentRepository
                    .findAll()
                    .stream()
                    .map(this::buildResponse)
                    .collect(Collectors.toList());
        }

        return documentRepository
                .findAll()
                .stream()
                .filter(document ->
                        document.getUploadedBy()
                                .getId()
                                .equals(
                                        authenticatedEmployee
                                                .getId()))
                .map(this::buildResponse)
                .collect(Collectors.toList());
    }

    /*
     * ==============================
     * GET DOCUMENT BY ID
     * ==============================
     */
    public DocumentResponse getDocumentById(
            Long id,
            Employee authenticatedEmployee) {

        Document document =
                documentRepository.findById(id)
                        .orElseThrow(() ->
                                new DocumentNotFoundException(
                                        "Document not found"));

        checkDocumentAccess(
                document,
                authenticatedEmployee);

        return buildResponse(
                document);
    }

    /*
     * ==============================
     * APPROVE DOCUMENT
     * ==============================
     */
    public DocumentResponse approveDocument(
            Long documentId,
            ApprovalRequest request,
            Employee authenticatedEmployee) {

        return updateDocumentStatus(
                documentId,
                request,
                "APPROVED",
                authenticatedEmployee);
    }

    /*
     * ==============================
     * REJECT DOCUMENT
     * ==============================
     */
    public DocumentResponse rejectDocument(
            Long documentId,
            ApprovalRequest request,
            Employee authenticatedEmployee) {

        return updateDocumentStatus(
                documentId,
                request,
                "REJECTED",
                authenticatedEmployee);
    }

    /*
     * ==============================
     * DOWNLOAD DOCUMENT
     * ==============================
     */
    public Resource downloadDocument(
            Long id,
            Employee authenticatedEmployee)
            throws MalformedURLException {

        Document document =
                documentRepository.findById(id)
                        .orElseThrow(() ->
                                new DocumentNotFoundException(
                                        "Document not found"));

        checkDocumentAccess(
                document,
                authenticatedEmployee);

        String filePath =
                document.getFilePath();

        /*
         * Approved documents use the
         * QR-stamped PDF.
         */
        if ("APPROVED".equals(
                document.getStatus())) {

            filePath =
                    filePath.replace(
                            ".pdf",
                            "_QR.pdf");
        }

        Path path =
                Paths.get(filePath);

        if (!Files.exists(path)) {

            throw new DocumentNotFoundException(
                    "Document file not found");
        }

        return new UrlResource(
                path.toUri());
    }

    /*
     * ==============================
     * ACCESS CONTROL
     * ==============================
     */
    private void checkDocumentAccess(
            Document document,
            Employee authenticatedEmployee) {

        String role =
                authenticatedEmployee
                        .getRole()
                        .getRoleName();

        boolean isAdminOrManager =
                role.equals("ADMIN")
                        ||
                role.equals("MANAGER");

        /*
         * Admin and Manager can access
         * all documents.
         */
        if (isAdminOrManager) {
            return;
        }

        /*
         * Employee can access only
         * their own document.
         */
        if (!document
                .getUploadedBy()
                .getId()
                .equals(
                        authenticatedEmployee
                                .getId())) {

            /*
             * Return "not found" instead of
             * exposing that another document exists.
             */
            throw new DocumentNotFoundException(
                    "Document not found");
        }
    }

    /*
     * ==============================
     * BUILD RESPONSE
     * ==============================
     */
    private DocumentResponse buildResponse(
            Document document) {

        DocumentResponse response =
                new DocumentResponse();

        response.setId(
                document.getId());

        response.setDocumentName(
                document.getDocumentName());

        response.setStatus(
                document.getStatus());

        response.setUploadedBy(
                document.getUploadedBy()
                        .getFirstName()
                        + " "
                        + document.getUploadedBy()
                                .getLastName());

        response.setCreatedAt(
                document.getCreatedAt());

        return response;
    }

    /*
     * ==============================
     * UPDATE STATUS
     * ==============================
     *
     * IMPORTANT:
     *
     * authenticatedEmployee is the
     * actual person performing the
     * approval/rejection.
     *
     * We do NOT trust managerId from
     * the request for this.
     */
    private DocumentResponse updateDocumentStatus(
            Long documentId,
            ApprovalRequest request,
            String status,
            Employee authenticatedEmployee) {

        Document document =
                documentRepository.findById(
                        documentId)
                        .orElseThrow(() ->
                                new DocumentNotFoundException(
                                        "Document not found"));

        /*
         * Only ADMIN / MANAGER should
         * reach this method through the
         * controller's @PreAuthorize.
         *
         * This additional check makes the
         * service safer as well.
         */
        String role =
                authenticatedEmployee
                        .getRole()
                        .getRoleName();

        if (!role.equals("ADMIN")
                && !role.equals("MANAGER")) {

            throw new SecurityException(
                    "User is not authorized to approve or reject documents");
        }

        /*
         * Update document status.
         */
        document.setStatus(
                status);

        /*
         * IMPORTANT:
         *
         * The authenticated user becomes
         * the approver/reviewer.
         */
        document.setApprovedBy(
                authenticatedEmployee);

        document.setApprovedAt(
                LocalDateTime.now());

        /*
         * Save approval/rejection
         * comments.
         */
        document.setComments(
                request.getComments());

        /*
         * Generate QR only when
         * document is APPROVED.
         */
        if ("APPROVED".equals(
                status)) {

            try {

                /*
                 * For now we keep the existing
                 * QR content so we don't change
                 * the working QR functionality.
                 */
                String qrPath =
                        qrPdfService.generateQrCode(
                                "HAL");

                qrPdfService.appendQrPage(
                        document.getFilePath(),
                        qrPath);

            } catch (Exception e) {

                /*
                 * Log the QR error but don't
                 * hide the document status update.
                 */
                e.printStackTrace();
            }
        }

        documentRepository.save(
                document);

        return buildResponse(
                document);
    }

    /*
     * ==============================
     * PAGINATED DOCUMENTS
     * ==============================
     */
    public Page<DocumentResponse> getDocuments(
            int page,
            int size) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size);

        return documentRepository
                .findAll(pageable)
                .map(this::buildResponse);
    }

    /*
     * ==============================
     * SEARCH BY STATUS
     * ==============================
     */
    public List<DocumentResponse> searchByStatus(
            String status) {

        return documentRepository
                .findByStatus(status)
                .stream()
                .map(this::buildResponse)
                .toList();
    }

    /*
     * ==============================
     * DASHBOARD STATISTICS
     * ==============================
     */
    public DashboardStatsResponse getDashboardStats() {

        DashboardStatsResponse response =
                new DashboardStatsResponse();

        response.setTotalDocuments(
                documentRepository.count());

        response.setApprovedDocuments(
                documentRepository.countByStatus(
                        "APPROVED"));

        response.setPendingDocuments(
                documentRepository.countByStatus(
                        "PENDING"));

        response.setRejectedDocuments(
                documentRepository.countByStatus(
                        "REJECTED"));

        return response;
    }
}