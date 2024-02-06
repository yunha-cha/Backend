package com.wittypuppy.backend.approval.service;

import com.wittypuppy.backend.Employee.dto.EmployeeDTO;
import com.wittypuppy.backend.Employee.entity.LoginEmployee;
import com.wittypuppy.backend.Employee.repository.EmployeeRepository;
import com.wittypuppy.backend.approval.dto.ApprovalDocDTO;
import com.wittypuppy.backend.approval.entity.ApprovalDoc;
import com.wittypuppy.backend.approval.repository.ApprovalDocRepository;
import com.wittypuppy.backend.approval.repository.ApprovalLineRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
public class ApprovalService {
    private final ModelMapper modelMapper;
    private final ApprovalDocRepository approvalDocRepository;
    private final EmployeeRepository employeeRepository;
    private final ApprovalLineRepository approvalLineRepository;


    public ApprovalService(ModelMapper modelMapper, ApprovalDocRepository approvalDocRepository, EmployeeRepository employeeRepository, ApprovalLineRepository approvalLineRepository) {
        this.modelMapper = modelMapper;
        this.approvalDocRepository = approvalDocRepository;
        this.employeeRepository = employeeRepository;
        this.approvalLineRepository = approvalLineRepository;
    }

    public ApprovalDocDTO selectInboxDoc(Long approvalDocCode) {
        log.info("[ApprovalService] selectInboxDoc start=====");

        ApprovalDoc approvalDoc = approvalDocRepository.findById(approvalDocCode).get();
        ApprovalDocDTO approvalDocDTO = modelMapper.map(approvalDoc, ApprovalDocDTO.class);

        log.info("[ApprovalService] selectInboxDoc End=====");

        return approvalDocDTO;
    }

    @Transactional
    public String submitApproval(ApprovalDocDTO approvalDocDTO, EmployeeDTO employeeDTO) {
        log.info("[ApprovalService] submitApproval start=====");

        int result = 0;

        // 문서 정보 저장
        ApprovalDoc newDoc = modelMapper.map(approvalDocDTO, ApprovalDoc.class);
        newDoc.setApprovalRequestDate(LocalDateTime.now());

        // 로그인한 사용자의 employeeCode를 가져와서 설정
        LoginEmployee loginEmployee = modelMapper.map(employeeDTO, LoginEmployee.class);
        newDoc.setEmployeeCode(loginEmployee);

        approvalDocRepository.save(newDoc);

//        // 결재선 정보 저장
//        ApprovalLine newApprovalLine = new ApprovalLine();
//        newApprovalLine.setApprovalDoc(newDoc);
//
//        approvalLineRepository.save(newApprovalLine);

        result = 1;

    return result > 0 ? "상신 성공" : "상신 실패";
    }
}
