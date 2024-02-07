package com.wittypuppy.backend.approval.controller;

import com.wittypuppy.backend.approval.service.ApprovalService;
import com.wittypuppy.backend.common.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/{approvalDocCode}")
    public ResponseEntity<ResponseDTO> selectInboxDoc(@PathVariable Long approvalDocCode){
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공", approvalService.selectInboxDoc(approvalDocCode)));
    }
}
