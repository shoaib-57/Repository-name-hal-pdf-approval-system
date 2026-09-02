package com.hal.pdf_approval_system.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /*
     * ==============================
     * 403 - ACCESS DENIED
     * ==============================
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(
            AccessDeniedException ex) {

        logger.warn(
                "Access denied: {}",
                ex.getMessage());

        return buildErrorResponse(
                HttpStatus.FORBIDDEN,
                "Access denied");
    }

    /*
     * ==============================
     * 400 - VALIDATION ERROR
     * ==============================
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors =
                new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()));

        return ResponseEntity
                .badRequest()
                .body(errors);
    }

    /*
     * ==============================
     * 413 - FILE TOO LARGE
     * ==============================
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiError> handleMaxUploadSizeExceeded(
            MaxUploadSizeExceededException ex) {

        logger.warn(
                "File upload exceeded maximum allowed size");

        return buildErrorResponse(
                HttpStatus.PAYLOAD_TOO_LARGE,
                "Uploaded file is too large");
    }

    /*
     * ==============================
     * MULTIPART UPLOAD ERROR
     * ==============================
     */
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ApiError> handleMultipartException(
            MultipartException ex) {

        logger.warn(
                "Multipart upload error: {}",
                ex.getMessage());

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid file upload request");
    }

    /*
     * ==============================
     * 404 - DOCUMENT NOT FOUND
     * ==============================
     */
    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<ApiError> handleDocumentNotFoundException(
            DocumentNotFoundException ex) {

        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage());
    }

    /*
     * ==============================
     * BUSINESS EXCEPTIONS
     * ==============================
     */
    @ExceptionHandler({
            EmployeeNotFoundException.class,
            ManagerNotFoundException.class,
            InvalidCredentialsException.class
    })
    public ResponseEntity<ApiError> handleBusinessExceptions(
            RuntimeException ex) {

        if (ex instanceof InvalidCredentialsException) {

            return buildErrorResponse(
                    HttpStatus.UNAUTHORIZED,
                    ex.getMessage());
        }

        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage());
    }

    /*
     * ==============================
     * GENERAL RUNTIME ERROR
     * ==============================
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntimeException(
            RuntimeException ex) {

        logger.error(
                "Runtime exception",
                ex);

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage() != null
                        ? ex.getMessage()
                        : "Request could not be processed");
    }

    /*
     * ==============================
     * FALLBACK EXCEPTION HANDLER
     * ==============================
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneralException(
            Exception ex) {

        logger.error(
                "Unexpected application error",
                ex);

        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred");
    }

    /*
     * ==============================
     * ERROR RESPONSE BUILDER
     * ==============================
     */
    private ResponseEntity<ApiError> buildErrorResponse(
            HttpStatus status,
            String message) {

        ApiError error =
                new ApiError();

        error.setTimestamp(
                LocalDateTime.now());

        error.setStatus(
                status.value());

        error.setError(
                status.getReasonPhrase());

        error.setMessage(
                message);

        return ResponseEntity
                .status(status)
                .body(error);
    }
}