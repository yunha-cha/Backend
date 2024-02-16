package com.wittypuppy.backend.attendance.service;

import com.wittypuppy.backend.Employee.dto.User;
import com.wittypuppy.backend.attendance.dto.*;
import com.wittypuppy.backend.attendance.entity.ApprovalLine;
import com.wittypuppy.backend.attendance.entity.AttendanceManagement;
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
import java.util.List;


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

    public Page<AttendanceManagementDTO> selectCommuteList(Criteria cri, String yearMonth, int employeeCode) {
        System.out.println("=============WorkTypeList start= service===============");

        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by(Sort.Direction.ASC, "attendance_management_code"));

        System.out.println("========= employeeCode ====== " + employeeCode);
        System.out.println("========== yearMonth ======= " + yearMonth);

        Page<AttendanceManagement> result = managementRepository.attendanceList(yearMonth, employeeCode, paging);

        Page<AttendanceManagementDTO> workTypeList = result.map(myDocumentWaiting -> modelMapper.map(myDocumentWaiting, AttendanceManagementDTO.class));

        System.out.println("============== result ================== " + result);
        System.out.println("WorkTypeList = " + workTypeList);
        System.out.println("========== WorkTypeList End ===========");

        return workTypeList;
    }




    public Page<ApprovalLineDTO> myDocumentWaitingList(Criteria cri, int employeeCode) {

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

        Page<ApprovalLineDTO> resultList = result.map(commute -> modelMapper.map(commute, ApprovalLineDTO.class));

        System.out.println("========resultList======= " + resultList);
        System.out.println("======== myDocumentWaitingList end ============");

        return resultList;
    }






    public Page<ApprovalLineDTO> myDocumentPaymentList(Criteria cri, int employeeCode) {
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


    public Page<ApprovalLineDTO> myDocumentCompanionList(Criteria cri, int employeeCode) {

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


    public Page<ApprovalLineDTO> paymentCompletedList(Criteria cri, int employeeCode) {

        /*
        * 내가 결재한 문서
        * 내 직원코드가 결재라인 직원코드가 동일
        * 상태값이 결재인 경우
        * */

        System.out.println("=====service=====paymentCompletedListStart========");
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by("approval_process_date").descending());

        System.out.println("======= employeeCode ======== " + employeeCode);

        Page<ApprovalLine> result = attendanceApprovalRepository.approvalPayment(paging, employeeCode);

        Page<ApprovalLineDTO> resultList = result.map(paymentCompleted -> modelMapper.map(paymentCompleted, ApprovalLineDTO.class));

        System.out.println("========resultList======= " + resultList);
        System.out.println("======== paymentCompletedList end ============");

        return resultList;
    }


    public Page<ApprovalLineDTO> paymentRejectionList(Criteria cri, int employeeCode) {

        /*
        * 내가 반려한 문서
         * 내 직원코드가 결재라인 직원코드가 동일
         * 상태값이 반려인 경우
        * */

        System.out.println("=====service=====paymentRejectionListStart========");
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by("approval_process_date").descending());

        System.out.println("======= employeeCode ======== " + employeeCode);

        Page<ApprovalLine> result = attendanceLineRepository.rejectionDocument(paging, employeeCode);

        Page<ApprovalLineDTO> resultList = result.map(paymentRejection -> modelMapper.map(paymentRejection, ApprovalLineDTO.class));

        System.out.println("========resultList======= " + resultList);
        System.out.println("======== paymentRejectionList end ============");

        return resultList;
    }

    public Page<ApprovalLineDTO> paymentWaitingList(Criteria cri, int employeeCode) {

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
    public AttendanceManagementDTO attendanceMain(int employeeCode) {

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
    public VacationDTO attendanceVacation(int employeeCode) {

        System.out.println(" =========== employeeCode ===========> " + employeeCode);
        System.out.println("========attendanceVacation ServiceStart======");

        Long total = managementRepository.attendanceTotalVacation(employeeCode);
        Long useVacation = managementRepository.attendanceUseVacation(employeeCode);
        Long useHalfVacation = managementRepository.attendanceUseHalfVacation(employeeCode);
        System.out.println("========== total ===========> " + total);
        System.out.println("=========== useVacation =========> " + useVacation);
        System.out.println("============== useHalfVacation ==========> " + useHalfVacation);

        int totalDays = total.intValue();
        int usedVacationDays = useVacation.intValue();
        int usedHalfVacationDays = useHalfVacation.intValue();

        double vacationDay = totalDays - usedVacationDays - (usedHalfVacationDays * 0.5);

        VacationDTO vacation = new VacationDTO();
        vacation.setTotal(totalDays);
        vacation.setUseVacation(usedVacationDays);
        vacation.setUseHalfVacation(usedHalfVacationDays);
        vacation.setResultVacation(vacationDay);

        System.out.println("========= 남은 연차 result ======== " + vacationDay);

        System.out.println("========attendanceVacation end ======");

        return vacation;

    }


    //근태 결재(대기) 할 수량 조회
    public ApprovalLineDTO attendanceWaiting(int employeeCode) {

        System.out.println(" =========== employeeCode ===========> " + employeeCode);
        System.out.println("========attendanceWaiting ServiceStart======");

        List<ApprovalLine> results = attendanceApprovalRepository.attendanceWaiting(employeeCode);

        // '대기' 상태인지 확인하고 갯수를 계산
        int waitingCount = 0;

        for (ApprovalLine approvalLine : results) {
            if (approvalLine != null && "대기".equals(approvalLine.getApprovalProcessStatus())) {
                waitingCount++;
            }
        }

        ApprovalLineDTO dto = new ApprovalLineDTO();
        dto.setCountWaiting(waitingCount);

        System.out.println("========== result ========> " + results);
        System.out.println("========attendanceWaiting end ======");
        return dto;

    }





    @Transactional
    public String insertArrival(User employeeCode, LocalDateTime arrivalTime, LocalDateTime departureTime, String status) {

    System.out.println("============== insertArrival ======> serviceStart ");
    System.out.println(" ======employeeCode ========== " + employeeCode);
    System.out.println("==== arrivalTime ======= " + arrivalTime);
    System.out.println("====== departureTime ====== " + departureTime);
    System.out.println("====== status ====== " + status);

    int result = 0;

    try {
        // 현재 날짜 가져오기
        LocalDate today = LocalDate.now();

        // 출근 정보를 담은 DTO 객체 생성
        AttendanceManagementDTO attendanceManagementDTO = new AttendanceManagementDTO();
        attendanceManagementDTO.setAttendanceEmployeeCode(employeeCode); // 로그인한 employeeCode 정보 설정
        attendanceManagementDTO.setAttendanceManagementArrivalTime(arrivalTime);
        attendanceManagementDTO.setAttendanceManagementDepartureTime(departureTime);
        attendanceManagementDTO.setAttendanceManagementState(status);
        attendanceManagementDTO.setAttendanceManagementWorkDay(today);
        attendanceManagementDTO.setAttendanceManagementCode(null);

        // DTO 객체를 Entity로 변환
        AttendanceManagement insertAttendance = modelMapper.map(attendanceManagementDTO, AttendanceManagement.class);

        System.out.println("========= insertAttendance ======= " + insertAttendance);
        // 저장소에 저장
        managementRepository.save(insertAttendance);
        result = 1;

        } catch (Exception e) {
            e.printStackTrace();
        }
            return result > 0 ? "출근 인서트 성공" : "출근 인서트 실패";
    }


    @Transactional
    public String updateDeparture(User employeeCode, LocalDateTime departureTime, String status) {

        System.out.println("===== employeeCode =====> " + employeeCode);
        System.out.println("========== departureTime ============ " + departureTime);
        System.out.println("============ status ============== " + status);

        int employeeNum = employeeCode.getEmployeeCode();
        System.out.println("========== employeeNum ==========> " + employeeNum);

        int result = 0;

        try {
            //가장 최근에 인처트 된 출근 시간 조회
            AttendanceManagement updateAttendance = managementRepository.findFirstByAttendanceEmployeeCode_EmployeeCodeOrderByAttendanceManagementCodeDesc(employeeNum);

            System.out.println("======= updateAttendance = " + updateAttendance);

            updateAttendance.setAttendanceManagementDepartureTime(departureTime);
            updateAttendance.setAttendanceManagementState(status);

            System.out.println("======= updateAttendance ======> " + updateAttendance);
            result = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result > 0 ? "퇴근시간 수정 성공" : "퇴근시간  수정 실패";
    }


    @Transactional
    public String updateOnlyDeparture(User employeeCode, LocalDateTime departureTime) {

        System.out.println("===== employeeCode =====> " + employeeCode);
        System.out.println("========== departureTime ============ " + departureTime);
        System.out.println("##### updateOnlyDeparture start = ");

        int employeeNum = employeeCode.getEmployeeCode();
        System.out.println("========== employeeNum ==========> " + employeeNum);

        int result = 0;

        try {
            //가장 최근에 인처트 된 출근 시간 조회
            AttendanceManagement updateAttendance = managementRepository.findFirstByAttendanceEmployeeCode_EmployeeCodeOrderByAttendanceManagementCodeDesc(employeeNum);

            System.out.println("======= updateAttendance = " + updateAttendance);

            updateAttendance.setAttendanceManagementDepartureTime(departureTime);

            System.out.println("======= updateAttendance ======> " + updateAttendance);
            result = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result > 0 ? "퇴근시간 수정 성공" : "퇴근시간  수정 실패";

    }


    public AttendanceManagementDTO countNormal(int employeeCode, String yearMonth) {

        List<AttendanceManagement> attendanceList = managementRepository.normal(employeeCode, yearMonth);

        System.out.println("======= attendanceList ======= " + attendanceList);

        int normalCount = 0;
        int lateCount = 0;
        int earlyCount = 0;

        for (AttendanceManagement attendance : attendanceList) {
            String state = attendance.getAttendanceManagementState();

            // attendanceManagementState 값에 따라 정상, 지각, 조퇴를 판단하여 횟수를 계산
            switch (state) {
                case "정상":
                    normalCount++;
                    break;
                case "지각":
                    lateCount++;
                    break;
                case "조퇴":
                    earlyCount++;
                    break;
                default:
                    // 처리하지 않는 상태 또는 오류 상태에 대한 처리
                    break;
            }
        }

        System.out.println("정상 출근 횟수: " + normalCount);
        System.out.println("지각 횟수: " + lateCount);
        System.out.println("조퇴 횟수: " + earlyCount);

        AttendanceManagementDTO attendanceManagementDTO = new AttendanceManagementDTO();
        attendanceManagementDTO.setNormal(normalCount);
        attendanceManagementDTO.setLate(lateCount);
        attendanceManagementDTO.setEarly(earlyCount);


        return attendanceManagementDTO;
    }



}
