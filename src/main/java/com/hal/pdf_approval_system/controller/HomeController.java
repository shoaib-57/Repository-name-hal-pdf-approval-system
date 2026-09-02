package com.hal.pdf_approval_system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "HAL PDF Approval System Backend is Running 🚀";
    }
}