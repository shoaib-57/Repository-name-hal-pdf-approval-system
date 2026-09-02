package com.hal.pdf_approval_system.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApprovalRequest {

    private Long managerId;

    private String comments;

}