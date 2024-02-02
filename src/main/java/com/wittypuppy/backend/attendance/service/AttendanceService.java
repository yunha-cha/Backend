package com.wittypuppy.backend.attendance.service;

import com.wittypuppy.backend.attendance.dto.ApprovalLineDTO;
import com.wittypuppy.backend.attendance.dto.AttendanceWorkTypeDTO;
import com.wittypuppy.backend.attendance.entity.ApprovalLine;
import com.wittypuppy.backend.attendance.entity.AttendanceManagement;
import com.wittypuppy.backend.attendance.entity.AttendanceWorkType;
import com.wittypuppy.backend.attendance.paging.Criteria;
import com.wittypuppy.backend.attendance.repository.ApprovalRepository;
import com.wittypuppy.backend.attendance.repository.WorkTypeRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;


@Service
@Slf4j
public class AttendanceService {


    private final WorkTypeRepository workTypeRepository;

    private final ModelMapper modelMapper;

    private final ApprovalRepository approvalRepository;

    private final LineRepository lineRepository;

    public AttendanceService(WorkTypeRepository workTypeRepository, ModelMapper modelMapper, ApprovalRepository approvalRepository, LineRepository lineRepository) {
        this.workTypeRepository = workTypeRepository;
        this.modelMapper = modelMapper;
        this.approvalRepository = approvalRepository;
        this.lineRepository = lineRepository;
    }

    public Page<AttendanceWorkTypeDTO> selectCommuteList(Criteria cri, String yearMonth, Long employeeCode) {
        System.out.println("=============WorkTypeList start= service===============");

        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by(Sort.Direction.ASC, "attendance_management_work_day"));

        System.out.println("========= employeeCode ====== " + employeeCode);
        System.out.println("========== yearMonth ======= " + yearMonth);

        Page<AttendanceWorkType> result = workTypeRepository.attendanceList(yearMonth, employeeCode, paging);

        Page<AttendanceWorkTypeDTO> workTypeList = result.map(attendance -> modelMapper.map(attendance, AttendanceWorkTypeDTO.class));

        System.out.println("WorkTypeList = " + workTypeList);
        System.out.println("========== WorkTypeList End ===========");

        return workTypeList;
    }



    public Page<ApprovalLineDTO> myDocumentWaitingList(Criteria cri, Long employeeCode) {

        /*
        * 내가 신청한 문서 기안
        *
        * 결재 순서 1번 째 인경우 (결재 직원코드가 로그인 정보 동일하면서)
        *
        * 철회(첫번째(2) 결재자가 결재 하기 전 ) -->
        * */

        System.out.println("=====service=====myDocumentWaitingListStart========");
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by("approval_document_code").descending());

        System.out.println("======= employeeCode ======== " + employeeCode);

        Page<ApprovalLine> result = approvalRepository.findByApplyDocument(employeeCode, paging);

        Page<ApprovalLineDTO> resultList = result.map(myDocumentWaiting -> modelMapper.map(myDocumentWaiting, ApprovalLineDTO.class));

        System.out.println("========resultList======= " + resultList);
        System.out.println("======== myDocumentWaitingList end ============");

        return resultList;
    }


    public Page<ApprovalLineDTO> myDocumentPaymentList(Criteria cri, Long employeeCode) {
        /*
        * 신청한 문서 결재 완료 상태
        * */

        System.out.println("=====service====myDocumentPaymentList========");
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by("approval_document_code").descending());
        System.out.println("employeeCode========> " + employeeCode);

        // approvalProcessOrder 전체를 고려하여 조회
        Page<ApprovalLine> result = approvalRepository.findMyDocumentPayment (employeeCode, paging);

        Page<ApprovalLineDTO> resultList = result.map(myDocumentWaiting -> modelMapper.map(myDocumentWaiting, ApprovalLineDTO.class));

        System.out.println("========resultList======= " + resultList);
        System.out.println("======== myDocumentWaitingList end ============");

        return resultList;


    }


    public Page<ApprovalLineDTO> myDocumentCompanionList(Criteria cri, Long employeeCode) {

        /*
         * 신청한 문서 반려 상태
         * 반려된 결재가 있는 경우
         * 전체 문서에서
         * 로그인 정보 직원코드가 결재라인 직원코드 동일하고 결재 상태가 반려인거
         * */

        System.out.println("=====service=====myDocumentCompanionListStart========");
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by("approval_document_code").descending());

        System.out.println("======= employeeCode ======== " + employeeCode);

        Page<ApprovalLine> result = approvalRepository.findByApprovalProcessStatusAndLineEmployeeCode_EmployeeCodeNative(paging, employeeCode);

        Page<ApprovalLineDTO> resultList = result.map(myDocumentCompanion -> modelMapper.map(myDocumentCompanion, ApprovalLineDTO.class));

        System.out.println("========resultList======= " + resultList);
        System.out.println("======== myDocumentCompanionList end ============");

        return resultList;
    }


    public Page<ApprovalLineDTO> paymentCompletedList(Criteria cri, Long employeeCode) {

        /*
        * 내가 결재한 문서
        * 내 직원코드가 결재라인 직원코드가 동일
        * 상태값이 결재인 경우
        * */

        System.out.println("=====service=====paymentCompletedListStart========");
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by("approvalLineCode").descending());

        System.out.println("======= employeeCode ======== " + employeeCode);

        Page<ApprovalLine> result = approvalRepository.findByLineEmployeeCode_employeeCodeAndApprovalProcessStatus(paging, employeeCode, "결재");

        Page<ApprovalLineDTO> resultList = result.map(paymentCompleted -> modelMapper.map(paymentCompleted, ApprovalLineDTO.class));

        System.out.println("========resultList======= " + resultList);
        System.out.println("======== paymentCompletedList end ============");

        return resultList;
    }


    public Page<ApprovalLineDTO> paymentRejectionList(Criteria cri, Long employeeCode) {

        /*
        * 내가 반려한 문서
         * 내 직원코드가 결재라인 직원코드가 동일
         * 상태값이 반려인 경우
        * */

        System.out.println("=====service=====paymentRejectionListStart========");
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by("approvalLineCode").descending());

        System.out.println("======= employeeCode ======== " + employeeCode);

        Page<ApprovalLine> result = lineRepository.findByLineEmployeeCode_employeeCodeAndApprovalProcessStatus(paging, employeeCode, "반려");

        Page<ApprovalLineDTO> resultList = result.map(paymentRejection -> modelMapper.map(paymentRejection, ApprovalLineDTO.class));

        System.out.println("========resultList======= " + resultList);
        System.out.println("======== paymentRejectionList end ============");

        return resultList;
    }

    public Page<ApprovalLineDTO> paymentWaitingList(Criteria cri, Long employeeCode) {

        /*
        * 대기 문서
        * 상태값 대기& 직원코드 하고 결재라인 직원코드 동일
        * 결재 순서 전 단계가 대기면 안 보여준다
        * 결재 순서 전 단계가 기안, 결재면 보여주기
        *
        * 승인, 반려 상태값 업데이트 하기
        * */

        System.out.println("=====service=====paymentWaitingListStart========");
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by("approval_document_code").descending());

        System.out.println("======= employeeCode ======== " + employeeCode);

        Page<ApprovalLine> result = approvalRepository.paymentWaiting(paging, employeeCode);

        Page<ApprovalLineDTO> resultList = result.map(paymentWaiting -> modelMapper.map(paymentWaiting, ApprovalLineDTO.class));

        System.out.println("========resultList======= " + resultList);
        System.out.println("======== paymentRejectionList end ============");

        return resultList;
    }

    public Object attendanceMain(Long employeeCode) {

        return null;
    }
}
