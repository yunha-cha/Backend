package com.wittypuppy.backend.approval.controller;

import com.wittypuppy.backend.Employee.dto.EmployeeDTO;
import com.wittypuppy.backend.approval.dto.AdditionalApprovalLineDTO;
import com.wittypuppy.backend.approval.dto.ApprovalDocDTO;
import com.wittypuppy.backend.approval.entity.ApprovalDoc;
import com.wittypuppy.backend.approval.service.ApprovalService;
import com.wittypuppy.backend.common.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/approval")
@Slf4j
public class ApprovalController {

    private final ApprovalService approvalService;

    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

//    @GetMapping("/{approvalDocCode}")
//    public ResponseEntity<ResponseDTO> selectInboxDoc(@PathVariable Long approvalDocCode){
//        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공", approvalService.selectInboxDoc(approvalDocCode)));
//    }

//     결재 문서 상신하기
    @PostMapping("/submit-approval")
    public ResponseEntity<ResponseDTO> submitApproval(@RequestBody ApprovalDocDTO approvalDocDTO, @AuthenticationPrincipal EmployeeDTO employeeDTO){
        ApprovalDoc savedApprovalDoc = approvalService.saveApprovalDoc(approvalDocDTO, employeeDTO);
        approvalService.saveApprovalLines(savedApprovalDoc);
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "상신 성공"));
    }

    // 상신한 문서 조회
    @GetMapping("/outbox-approval")
    public ResponseEntity<ResponseDTO> outboxApproval(@AuthenticationPrincipal EmployeeDTO employeeDTO) {
        List<ApprovalDoc> approvalDocs = approvalService.findApprovalDocsByEmployeeCode(employeeDTO);
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공", approvalDocs));
    }
}
