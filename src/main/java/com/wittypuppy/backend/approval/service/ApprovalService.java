package com.wittypuppy.backend.approval.service;

import com.wittypuppy.backend.Employee.dto.EmployeeDTO;
import com.wittypuppy.backend.Employee.entity.LoginEmployee;
import com.wittypuppy.backend.approval.dto.ApprovalDocDTO;
import com.wittypuppy.backend.approval.entity.AdditionalApprovalLine;
import com.wittypuppy.backend.approval.entity.ApprovalDoc;
import com.wittypuppy.backend.approval.entity.ApprovalLine;
import com.wittypuppy.backend.approval.repository.AdditionalApprovalLineRepository;
import com.wittypuppy.backend.approval.repository.ApprovalDocRepository;
import com.wittypuppy.backend.approval.repository.ApprovalLineRepository;
import com.wittypuppy.backend.attendance.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ApprovalService {
    private final ModelMapper modelMapper;
    private final ApprovalDocRepository approvalDocRepository;
    private final ApprovalLineRepository approvalLineRepository;
    private final AdditionalApprovalLineRepository additionalApprovalLineRepository;


    public ApprovalService(ModelMapper modelMapper, ApprovalDocRepository approvalDocRepository, ApprovalLineRepository approvalLineRepository, AdditionalApprovalLineRepository additionalApprovalLineRepository) {
        this.modelMapper = modelMapper;
        this.approvalDocRepository = approvalDocRepository;
        this.approvalLineRepository = approvalLineRepository;
        this.additionalApprovalLineRepository = additionalApprovalLineRepository;
    }

//    public ApprovalDocDTO selectInboxDoc(Long approvalDocCode) {
//        log.info("[ApprovalService] selectInboxDoc start=====");
//
//        ApprovalDoc approvalDoc = approvalDocRepository.findById(approvalDocCode).get();
//        ApprovalDocDTO approvalDocDTO = modelMapper.map(approvalDoc, ApprovalDocDTO.class);
//
//        log.info("[ApprovalService] selectInboxDoc End=====");
//
//        return approvalDocDTO;
//    }

//    @Transactional
//    public String submitApproval(ApprovalDocDTO approvalDocDTO, EmployeeDTO employeeDTO) {
//        log.info("[ApprovalService] submitApproval start=====");
//
//        int result = 0;
//
//        // 문서 정보 저장
//        ApprovalDoc newDoc = modelMapper.map(approvalDocDTO, ApprovalDoc.class);
//        newDoc.setApprovalRequestDate(LocalDateTime.now());
//
//        // 로그인한 사용자의 employeeCode를 가져와서 설정
//        LoginEmployee loginEmployee = modelMapper.map(employeeDTO, LoginEmployee.class);
//        newDoc.setEmployeeCode(loginEmployee);
//
//        approvalDocRepository.save(newDoc);
//
//        result = 1;
//
//    return result > 0 ? "상신 성공" : "상신 실패";
//    }


    // 기안 문서 정보 저장
    public ApprovalDoc saveApprovalDoc(ApprovalDocDTO approvalDocDTO, EmployeeDTO employeeDTO) {
        log.info("[ApprovalService] saving doc info started =====");

        ApprovalDoc approvalDoc = modelMapper.map(approvalDocDTO, ApprovalDoc.class);
        approvalDoc.setApprovalForm("SW사용신청서");

        LoginEmployee loginEmployee = modelMapper.map(employeeDTO, LoginEmployee.class);
        approvalDoc.setEmployeeCode(loginEmployee);

        approvalDoc.setApprovalRequestDate(LocalDateTime.now());
        approvalDoc.setWhetherSavingApproval("N");

        return approvalDocRepository.save(approvalDoc);
    }

    // 기안자 결재선 저장
    public void saveFirstApprovalLine(ApprovalDoc savedApprovalDoc, EmployeeDTO employeeDTO) {
        log.info("[ApprovalService] saving first approval line started =====");
        ApprovalLine approvalLine = new ApprovalLine();
        approvalLine.setApprovalDocCode(savedApprovalDoc.getApprovalDocCode());

        LoginEmployee loginEmployee = modelMapper.map(employeeDTO, LoginEmployee.class);
        approvalLine.setEmployeeCode((long) loginEmployee.getEmployeeCode());

        approvalLine.setApprovalProcessOrder(1L);
        approvalLine.setApprovalProcessStatus("기안");
        approvalLine.setApprovalProcessDate(LocalDateTime.now());
        approvalLine.setApprovalRejectedReason(null);
        approvalLineRepository.save(approvalLine);
    }

    // 추가 결재선 저장
    public void saveApprovalLines(ApprovalDoc savedApprovalDoc){
        log.info("[ApprovalService] saving line info started =====");
        AdditionalApprovalLine additionalApprovalLine = new AdditionalApprovalLine();
        additionalApprovalLine.setApprovalDocCode(savedApprovalDoc.getApprovalDocCode());
        additionalApprovalLine.setEmployeeCode(31L);
        additionalApprovalLine.setApprovalProcessOrder(2L);
        additionalApprovalLine.setApprovalProcessStatus("대기");
        additionalApprovalLine.setApprovalRejectedReason(null);
        additionalApprovalLineRepository.save(additionalApprovalLine);
    }

    // 상신한 문서 조회
    public List<ApprovalDoc> findApprovalDocsByEmployeeCode(EmployeeDTO employeeDTO) {
        // 로그인한 사용자의 정보 가져오기
        LoginEmployee loginEmployee = modelMapper.map(employeeDTO, LoginEmployee.class);

        // 해당 사용자의 결재 상신 문서 리스트 조회
        List<ApprovalDoc> outboxDocList = approvalDocRepository.findByEmployeeCode(loginEmployee);

        return outboxDocList;
    }

    // 결재 대기 문서 조회
    public List<ApprovalDoc> inboxDocListByEmployeeCode(EmployeeDTO employeeDTO){
        // 로그인한 사용자의 정보 가져오기
        LoginEmployee loginEmployee = modelMapper.map(employeeDTO, LoginEmployee.class);

        // 문서 코드 목록 가져오기
        List<Long> inboxDocCodeList = approvalDocRepository.inboxDocListByEmployeeCode(Long.valueOf(loginEmployee.getEmployeeCode()));

        // 문서 코드 목록으로 ApprovalDoc 정보 가져오기
        List<ApprovalDoc> inboxDocs = new ArrayList<>();
        for (Long approvalDocCode : inboxDocCodeList) {
            ApprovalDoc approvalDoc = approvalDocRepository.findByApprovalDocCode(approvalDocCode);
            if(approvalDoc != null) {
                inboxDocs.add(approvalDoc);
            }
        }
        return inboxDocs;
    }

    // 결재하기
    @Transactional
    public String approvement(Long approvalDocCode, EmployeeDTO employeeDTO) {

        // 결재 대상 조회
        Long approvalSubject = additionalApprovalLineRepository.approvalSubjectEmployeeCode(approvalDocCode);

        System.out.println("approvalSubject ========== " + approvalSubject);
        System.out.println("approvalSubject.getClass() = " + approvalSubject.getClass());

        // 로그인한 사용자의 정보 가져오기
        LoginEmployee loginEmployee = modelMapper.map(employeeDTO, LoginEmployee.class);
        Long employeeCode = (long) loginEmployee.getEmployeeCode();

        System.out.println("loginEmployee.getEmployeeCode() = " + loginEmployee.getEmployeeCode());
        System.out.println("loginEmployee.getClass() = " + loginEmployee.getClass());

        System.out.println("employeeCode ======== " + employeeCode);

        // 결재 대상과 로그인한 사용자 employeeCode 비교
        if (!approvalSubject.equals(employeeCode)) {
            return "결재 대상이 아닙니다.";
        }

        AdditionalApprovalLine additionalApprovalLine = additionalApprovalLineRepository.findByApprovalDocCodeAndEmployeeCode(approvalDocCode, employeeCode);

        System.out.println("additionalApprovalLine ======= " + additionalApprovalLine);

        // 결재 상태 업데이트
        additionalApprovalLine.setApprovalProcessDate(LocalDateTime.now());
        additionalApprovalLine.setApprovalProcessStatus("결재");
        additionalApprovalLineRepository.save(additionalApprovalLine);

        return "결재 성공";
    }

    // 반려하기
    public String rejection(Long approvalDocCode, EmployeeDTO employeeDTO) {

        // 반려 대상 조회
        Long approvalSubject = additionalApprovalLineRepository.approvalSubjectEmployeeCode(approvalDocCode);

        System.out.println("approvalSubject ========== " + approvalSubject);
        System.out.println("approvalSubject.getClass() = " + approvalSubject.getClass());

        // 로그인한 사용자의 정보 가져오기
        LoginEmployee loginEmployee = modelMapper.map(employeeDTO, LoginEmployee.class);
        Long employeeCode = (long) loginEmployee.getEmployeeCode();

        System.out.println("loginEmployee.getEmployeeCode() = " + loginEmployee.getEmployeeCode());
        System.out.println("loginEmployee.getClass() = " + loginEmployee.getClass());

        System.out.println("employeeCode ======== " + employeeCode);

        // 반려 대상과 로그인한 사용자 employeeCode 비교
        if (!approvalSubject.equals(employeeCode)) {
            return "반려 대상이 아닙니다.";
        }

        // 대기 상태인 모든 approvalLine 조회
        List<Long> pendingApprovalLines = additionalApprovalLineRepository.findPendingApprovalLines(approvalDocCode);

        // 각 approvalLine의 상태를 반려로 변경
        for (Long approvalLineCode : pendingApprovalLines) {
            Optional<AdditionalApprovalLine> optionalApprovalLine = additionalApprovalLineRepository.findById(approvalLineCode);
            optionalApprovalLine.ifPresent(approvalLine -> {
                approvalLine.setApprovalProcessDate(LocalDateTime.now());
                approvalLine.setApprovalProcessStatus("반려");
                additionalApprovalLineRepository.save(approvalLine);
            });
        }

        return "반려 성공";
    }
}
