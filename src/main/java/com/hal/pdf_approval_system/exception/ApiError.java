package com.hal.pdf_approval_system.exception;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiError {

    private LocalDateTime timestamp;

    private int status;

    private String error;

    private String message;

}