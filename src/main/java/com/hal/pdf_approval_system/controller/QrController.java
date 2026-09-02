package com.hal.pdf_approval_system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hal.pdf_approval_system.service.QrPdfService;

@RestController
public class QrController {

    private final QrPdfService qrPdfService;

    public QrController(QrPdfService qrPdfService) {
        this.qrPdfService = qrPdfService;
    }

    @GetMapping("/test-qr")
    public String testQr() throws Exception {

        return qrPdfService.generateQrCode(
                "HAL APPROVED DOCUMENT");
    }

}