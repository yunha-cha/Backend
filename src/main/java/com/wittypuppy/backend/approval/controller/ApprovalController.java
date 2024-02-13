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

    // 결재 문서 상신하기
    @PostMapping("/submit-approval")
    public ResponseEntity<ResponseDTO> submitApproval(ApprovalDocDTO approvalDocDTO, @AuthenticationPrincipal EmployeeDTO employeeDTO){
        ApprovalDoc savedApprovalDoc = approvalService.saveApprovalDoc(approvalDocDTO, employeeDTO);
        approvalService.saveOnLeaveDoc(savedApprovalDoc);
        approvalService.saveFirstApprovalLine(savedApprovalDoc, employeeDTO);
        approvalService.saveApprovalLines(savedApprovalDoc);
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "상신 성공"));
    }

    // 상신한 문서 조회
    @GetMapping("/outbox-approval")
    public ResponseEntity<ResponseDTO> outboxApproval(@AuthenticationPrincipal EmployeeDTO employeeDTO) {
        List<ApprovalDoc> approvalDocs = approvalService.findApprovalDocsByEmployeeCode(employeeDTO);
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공", approvalDocs));
    }

    // 결재 대기 문서 조회
    @GetMapping("/inbox-approval")
    public ResponseEntity<ResponseDTO> inboxApproval(@AuthenticationPrincipal EmployeeDTO employeeDTO) {
        List<ApprovalDoc> approvalDocs = approvalService.inboxDocListByEmployeeCode(employeeDTO);
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공", approvalDocs));
    }

    // 결재하기
    @PutMapping("/approvement/{approvalDocCode}")
    public ResponseEntity<String> approvement(@PathVariable Long approvalDocCode, @AuthenticationPrincipal EmployeeDTO employeeDTO){
        System.out.println("approvalDocCode = " + approvalDocCode);
        System.out.println("em = " + employeeDTO.getEmployeeCode());

        String result = approvalService.approvement(approvalDocCode, employeeDTO);
        System.out.println("result ========== " + result);
        return ResponseEntity.ok(result);
    }

    // 반려하기
    @PutMapping("/rejection/{approvalDocCode}")
    public ResponseEntity<String> rejection(@PathVariable Long approvalDocCode, @AuthenticationPrincipal EmployeeDTO employeeDTO){
        System.out.println("approvalDocCode = " + approvalDocCode);
        System.out.println("em = " + employeeDTO.getEmployeeCode());

        String result = approvalService.rejection(approvalDocCode, employeeDTO);
        System.out.println("result ========== " + result);
        return ResponseEntity.ok(result);
    }

    // 상신 문서 회수
    @PutMapping("/retrieval/{approvalDocCode}")
    public ResponseEntity<String> retrieval(@PathVariable Long approvalDocCode, @AuthenticationPrincipal EmployeeDTO employeeDTO){
        String result = approvalService.retrieval(approvalDocCode, employeeDTO);
        System.out.println("result ========== " + result);
        return ResponseEntity.ok(result);
    }

    // 결재 진행 중인 문서 조회
    @GetMapping("/outbox-on-process")
    public ResponseEntity<ResponseDTO> onProcessInOutbox(@AuthenticationPrincipal EmployeeDTO employeeDTO) {
        List<ApprovalDoc> approvalDocs = approvalService.onProcessInOutbox(employeeDTO);
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공", approvalDocs));
    }

    // 결재 완료 문서 조회
    @GetMapping("/outbox-finished")
    public ResponseEntity<ResponseDTO> finishedInOutbox(@AuthenticationPrincipal EmployeeDTO employeeDTO) {
        List<ApprovalDoc> approvalDocs = approvalService.finishedInOutbox(employeeDTO);
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공", approvalDocs));
    }

    // 반려 문서 조회
    @GetMapping("/outbox-rejected")
    public ResponseEntity<ResponseDTO> rejectedInOutbox(@AuthenticationPrincipal EmployeeDTO employeeDTO) {
        List<ApprovalDoc> approvalDocs = approvalService.rejectedInOutbox(employeeDTO);
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공", approvalDocs));
    }

    // 회수 문서 조회
    @GetMapping("/outbox-retrieved")
    public ResponseEntity<ResponseDTO> retrievedInOutbox(@AuthenticationPrincipal EmployeeDTO employeeDTO) {
        List<ApprovalDoc> approvalDocs = approvalService.retrievedInOutbox(employeeDTO);
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공", approvalDocs));
    }

    // 임시 저장
    @PostMapping("/save-approval")
    public ResponseEntity<ResponseDTO> saveApproval(ApprovalDocDTO approvalDocDTO, @AuthenticationPrincipal EmployeeDTO employeeDTO){
        approvalService.temporarySaveApprovalDoc(approvalDocDTO, employeeDTO);
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "저장 성공"));
    }

    // 임시 저장 문서 리스트 조회
    @GetMapping("/outbox-saved")
    public ResponseEntity<ResponseDTO> savedOutbox(@AuthenticationPrincipal EmployeeDTO employeeDTO) {
        List<ApprovalDoc> approvalDocs = approvalService.findSavedDocsByEmployeeCode(employeeDTO);
        System.out.println("approvalDocs = " + approvalDocs);
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공", approvalDocs));
    }

    // 수신함 - 결재 완료함

    // 열람자 지정하기

    // 열람함 문서 조회

    // 대리결재자 지정하기

    // 수신함 - 대리결재

    // 결재 문서 내용 추가

    // 휴가 일수 차감

    // 결재선 삭제

    // 결재선 순서 변경

    // 재기안
}
