package com.hal.pdf_approval_system.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardStatsResponse {

    private long totalDocuments;
    private long approvedDocuments;
    private long pendingDocuments;
    private long rejectedDocuments;
}