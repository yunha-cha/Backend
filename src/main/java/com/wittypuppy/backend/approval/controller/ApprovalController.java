package com.wittypuppy.backend.approval.controller;

import com.wittypuppy.backend.approval.dto.ApprovalDocDTO;
import com.wittypuppy.backend.approval.service.ApprovalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/approval")
@Slf4j
public class ApprovalController {

    private final ApprovalService approvalService;

    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    // 결재 상신
    @PostMapping("/submit-approval")
    public ResponseEntity<ApprovalDocDTO> submitApproval(@RequestBody ApprovalDocDTO approvalDocDTO) {
        ApprovalDocDTO submittedDoc = approvalService.submitApproval(approvalDocDTO);

        return null;
    }
}
