package com.wittypuppy.backend.attendance.service;

import com.wittypuppy.backend.attendance.dto.ApprovalLineDTO;
import com.wittypuppy.backend.attendance.dto.AttendanceManagementDTO;
import com.wittypuppy.backend.attendance.dto.AttendanceWorkTypeDTO;
import com.wittypuppy.backend.attendance.dto.VacationDTO;
import com.wittypuppy.backend.attendance.entity.ApprovalLine;
import com.wittypuppy.backend.attendance.entity.AttendanceManagement;
import com.wittypuppy.backend.attendance.entity.AttendanceWorkType;
import com.wittypuppy.backend.attendance.paging.Criteria;
import com.wittypuppy.backend.attendance.repository.AttendanceApprovalRepository;
import com.wittypuppy.backend.attendance.repository.AttendanceLineRepository;
import com.wittypuppy.backend.attendance.repository.CommuteWorkTypeRepository;
import com.wittypuppy.backend.attendance.repository.ManagementRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Service
@Slf4j
public class AttendanceService {


    private final CommuteWorkTypeRepository commuteWorkTypeRepository;

    private final ModelMapper modelMapper;

    private final AttendanceApprovalRepository attendanceApprovalRepository;

    private final AttendanceLineRepository attendanceLineRepository;

    private final ManagementRepository managementRepository;

    public AttendanceService(CommuteWorkTypeRepository commuteWorkTypeRepository, ModelMapper modelMapper, AttendanceApprovalRepository attendanceApprovalRepository, AttendanceLineRepository attendanceLineRepository, ManagementRepository managementRepository) {
        this.commuteWorkTypeRepository = commuteWorkTypeRepository;
        this.modelMapper = modelMapper;
        this.attendanceApprovalRepository = attendanceApprovalRepository;
        this.attendanceLineRepository = attendanceLineRepository;
        this.managementRepository = managementRepository;
    }

    public Page<AttendanceWorkTypeDTO> selectCommuteList(Criteria cri, String yearMonth, Long employeeCode) {
        System.out.println("=============WorkTypeList start= service===============");

        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by(Sort.Direction.ASC, "attendance_management_code"));

        System.out.println("========= employeeCode ====== " + employeeCode);
        System.out.println("========== yearMonth ======= " + yearMonth);

        Page<AttendanceWorkType> result = commuteWorkTypeRepository.attendanceList(yearMonth, employeeCode, paging);

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

        Page<ApprovalLine> result = attendanceApprovalRepository.findByApplyDocument(employeeCode, paging);

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
        Page<ApprovalLine> result = attendanceApprovalRepository.findMyDocumentPayment (employeeCode, paging);

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

        Page<ApprovalLine> result = attendanceApprovalRepository.findByApprovalProcessStatusAndLineEmployeeCode_EmployeeCodeNative(paging, employeeCode);

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

        Page<ApprovalLine> result = attendanceApprovalRepository.findByLineEmployeeCode_employeeCodeAndApprovalProcessStatus(paging, employeeCode, "결재");

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

        Page<ApprovalLine> result = attendanceLineRepository.findByLineEmployeeCode_employeeCodeAndApprovalProcessStatus(paging, employeeCode, "반려");

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
        * 승인, 반려 상태값 업데이트 하기 -->리액트에서 같이 하기
        * */

        System.out.println("=====service=====paymentWaitingLis tStart========");
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by("approval_document_code").descending());

        System.out.println("======= employeeCode ======== " + employeeCode);

        Page<ApprovalLine> result = attendanceApprovalRepository.paymentWaiting(paging, employeeCode);

        Page<ApprovalLineDTO> resultList = result.map(paymentWaiting -> modelMapper.map(paymentWaiting, ApprovalLineDTO.class));

        System.out.println("========resultList======= " + resultList);
        System.out.println("======== paymentWaitingLis end ============");

        return resultList;
    }



    //근태 메인 출퇴근 정보 조회
    public AttendanceManagementDTO attendanceMain(Long employeeCode) {

        System.out.println(" =========== employeeCode ===========> " + employeeCode);
        System.out.println("========attendanceMainServiceStart======");

        AttendanceManagement commute = managementRepository.attendanceCommute(employeeCode);

        // commute 값이 null인지 체크
        if (commute == null) {
            // null인 경우, 시간을 00:00:00으로 설정
            commute = new AttendanceManagement();
            commute.setAttendanceManagementArrivalTime(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT));
            // 또는 다른 필요한 처리 수행
        }

        AttendanceManagementDTO commutes = modelMapper.map(commute, AttendanceManagementDTO.class);

        System.out.println("========attendanceMainService end ======");
        return commutes;
    }


    //근태 메인 연차 남은 수량 조회
    public VacationDTO attendanceVacation(Long employeeCode) {

        System.out.println(" =========== employeeCode ===========> " + employeeCode);
        System.out.println("========attendanceVacation ServiceStart======");

        Long total = managementRepository.attendanceTotalVacation(employeeCode);
        Long useVacation = managementRepository.attendanceUseVacation(employeeCode);
        Long useHalfVacation = managementRepository.attendanceUseHalfVacation(employeeCode);

        System.out.println("========== total ===========> " + total);
        System.out.println("=========== useVacation =========> " + useVacation);
        System.out.println("============== useHalfVacation ==========> " + useHalfVacation);

        double result = total - useVacation - (useHalfVacation * 0.5);

        VacationDTO results = modelMapper.map(result, VacationDTO.class);

        System.out.println("========= 남은 연차 result ======== " + result);

        System.out.println("========attendanceVacation end ======");

        return results;

    }


    //근태 결재(대기) 할 수량 조회
    public ApprovalLineDTO attendanceWaiting(Long employeeCode) {

        System.out.println(" =========== employeeCode ===========> " + employeeCode);
        System.out.println("========attendanceWaiting ServiceStart======");

//        ApprovalLine result = attendanceApprovalRepository.attendanceWaiting(employeeCode);
//
//        ApprovalLineDTO results = modelMapper.map(result, ApprovalLineDTO.class);
//
//        System.out.println("========== result ========> " + result);
//        System.out.println("========attendanceWaiting end ======");
//
//        return results;

        return null;
    }



@Transactional
    public String insertArrival(Long employeeCode, AttendanceManagementDTO attendanceManagementDTO) {

        System.out.println("============== insertArrival ======> serviceStart ");
        System.out.println(" ======employeeCode ========== " + employeeCode);
        System.out.println("========= attendanceManagementDTO ========== " + attendanceManagementDTO);

        int result = 0;

        LocalDateTime arrival = attendanceManagementDTO.getAttendanceManagementArrivalTime();

        System.out.println("==== arrival = " + arrival);

        try {

            AttendanceManagement insertAttendance = modelMapper.map(attendanceManagementDTO, AttendanceManagement.class);

            managementRepository.save(insertAttendance);

            result = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result > 0 ? "출근 등록 성공" : "출근 등록 실패";
    }


    @Transactional
    public String updateDeparture(Long employeeCode, AttendanceManagementDTO attendanceManagementDTO) {
        System.out.println("===== employeeCode =====> " + employeeCode);
        System.out.println("======= attendanceManagementDTO ========= " + attendanceManagementDTO);

        int result = 0;

        try {
            //가장 최근에 인처트 된 출근 시간 조회
            AttendanceManagement updateAttendance = managementRepository.findFirstByAttendanceEmployeeCode_EmployeeCodeOrderByAttendanceManagementCodeDesc(employeeCode);

            System.out.println("======= updateAttendance = " + updateAttendance);

            System.out.println("=========> " + attendanceManagementDTO.getAttendanceManagementDepartureTime());

            updateAttendance.setAttendanceManagementDepartureTime(attendanceManagementDTO.getAttendanceManagementDepartureTime());
            updateAttendance.setAttendanceManagementState(attendanceManagementDTO.getAttendanceManagementState());


            System.out.println("======= updateAttendance ======> " + updateAttendance);
            result = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result > 0 ? "퇴근시간 수정 성공" : "퇴근시간  수정 실패";
    }


}
