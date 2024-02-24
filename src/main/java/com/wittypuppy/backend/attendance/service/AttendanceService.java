package com.wittypuppy.backend.attendance.service;

import com.wittypuppy.backend.Employee.dto.User;
import com.wittypuppy.backend.attendance.adminAttend.AdminEmployee;
import com.wittypuppy.backend.attendance.adminAttend.AdminEmployeeDTO;
import com.wittypuppy.backend.attendance.dto.*;
import com.wittypuppy.backend.attendance.entity.*;
import com.wittypuppy.backend.attendance.paging.Criteria;
import com.wittypuppy.backend.attendance.repository.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
@Slf4j
public class AttendanceService {


    private final AttendanceEmployeeRepository attendanceEmployeeRepository;

    private final ModelMapper modelMapper;

    private final AttendanceApprovalRepository attendanceApprovalRepository;

    private final AttendanceLineRepository attendanceLineRepository;

    private final ManagementRepository managementRepository;

    private final InsertCommuteRepository insertCommuteRepository;

    private final DetailMyRepository detailMyRepository;

    private final AttendanceOnLeaveRepository attendanceOnLeaveRepository;

    private final AttendanceOverWorkRepository attendanceOverWorkRepository;

    private final DocumentWorkType documentWorkType;

    private final AttendanceSoft attendanceSoft;

    private final AttendanceVaca attendanceVaca;

    public AttendanceService(AttendanceEmployeeRepository attendanceEmployeeRepository, ModelMapper modelMapper, AttendanceApprovalRepository attendanceApprovalRepository, AttendanceLineRepository attendanceLineRepository, ManagementRepository managementRepository, InsertCommuteRepository insertCommuteRepository, DetailMyRepository detailMyRepository, AttendanceOnLeaveRepository attendanceOnLeaveRepository, AttendanceOverWorkRepository attendanceOverWorkRepository, DocumentWorkType documentWorkType, AttendanceSoft attendanceSoft, AttendanceVaca attendanceVaca) {
        this.attendanceEmployeeRepository = attendanceEmployeeRepository;
        this.modelMapper = modelMapper;
        this.attendanceApprovalRepository = attendanceApprovalRepository;
        this.attendanceLineRepository = attendanceLineRepository;
        this.managementRepository = managementRepository;
        this.insertCommuteRepository = insertCommuteRepository;
        this.detailMyRepository = detailMyRepository;
        this.attendanceOnLeaveRepository = attendanceOnLeaveRepository;
        this.attendanceOverWorkRepository = attendanceOverWorkRepository;
        this.documentWorkType = documentWorkType;
        this.attendanceSoft = attendanceSoft;
        this.attendanceVaca = attendanceVaca;
    }

    public Page<AttendanceManagementDTO> selectCommuteList(Criteria cri, String yearMonth, int employeeCode) {
        System.out.println("=============WorkTypeList start= service===============");

        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by(Sort.Direction.DESC, "attendance_management_code"));

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
        Pageable paging = PageRequest.of(index, count, Sort.by("approval_process_date").descending());

        System.out.println("======= employeeCode ======== " + employeeCode);

        Page<ApprovalLine> myWaiting = attendanceApprovalRepository.findByApplyDocument(employeeCode, paging);

        Page<ApprovalLineDTO> resultList = myWaiting.map(commute -> modelMapper.map(commute, ApprovalLineDTO.class));

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
        Page<ApprovalLine> result = attendanceApprovalRepository.findMyDocumentPayment(employeeCode, paging);

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



        Employee vacationCount = attendanceEmployeeRepository.findByEmployeeCode(employeeCode);
        LocalDateTime join = vacationCount.getEmployeeJoinDate();
        System.out.println("입사일 -----> join = " + join);

        long daysSinceJoin = Duration.between(join, LocalDateTime.now()).toDays(); // 입사일부터 현재까지 경과한 일수 계산
        System.out.println("경과 일수 ----------> daysSinceJoin = " + daysSinceJoin);

        long yearsSinceJoin = daysSinceJoin / 365; // 현재까지 경과한 연 수
        System.out.println("연차 --------> yearsSinceJoin = " + yearsSinceJoin);

        int total = 0; //전체 수량 담을 것

        LocalDate currentDate = LocalDate.now();

        int currentDateYear = currentDate.getYear();

        LocalDateTime futureDate = null;

        if(daysSinceJoin >= 365) {

            if(yearsSinceJoin == 1){
                total = 15;
            }else {
                // 1년 이상 경과 후부터는 2년에 1개씩 증가
                total = 15 + (int) ((yearsSinceJoin - 1) / 2);
                System.out.println(" 연차 갯수 1 ===== total = " + total);
            }
        } else {
            for (int i = 1; i < 12; i++) {
                futureDate = join.plusMonths(i);
                System.out.println(" 월차 생성 일 futureDate = " + futureDate);
                if(futureDate.isBefore(currentDate.atStartOfDay())){
                    total = i;
                    System.out.println(" 월차 갯수 ===== total = " + total);
                }
            }
        }

        System.out.println(futureDate.getYear());

        Long useVacation = managementRepository.attendanceUseVacation(employeeCode);
        Long useHalfVacation = managementRepository.attendanceUseHalfVacation(employeeCode);
        System.out.println("========== total ===========> " + total);
        System.out.println("=========== useVacation =========> " + useVacation);
        System.out.println("============== useHalfVacation ==========> " + useHalfVacation);

        int usedVacationDays = useVacation.intValue(); //사용한 연차 수량
        int usedHalfVacationDays = useHalfVacation.intValue(); //사용한 반차 수량

        double nowVacation =0;

        LocalDateTime marchFirst = currentDate.withMonth(3).withDayOfMonth(1).atStartOfDay().minusDays(1); //그해 연차 사용 만료일


        if (currentDate.isBefore(ChronoLocalDate.from(marchFirst)) ){

            if (futureDate.getYear() < currentDateYear) {
                double vacationDay = total - usedVacationDays - (usedHalfVacationDays * 0.5);  // 전 년도 남은 연차 수량
                nowVacation = vacationDay + total;
            } else if (futureDate.getYear() == currentDateYear) {
                nowVacation = total - usedVacationDays - (usedHalfVacationDays * 0.5);  // 남은 연차 수량
            }

        } else if (currentDate.isAfter(ChronoLocalDate.from(marchFirst))) {

            nowVacation = total - usedVacationDays - (usedHalfVacationDays * 0.5);  // 남은 연차 수량
        }




        System.out.println("현재 수량 ===== nowVacation = " + nowVacation);

        VacationDTO vacation = new VacationDTO();
        vacation.setTotal(total);
        vacation.setUseVacation(usedVacationDays);
        vacation.setUseHalfVacation(usedHalfVacationDays);
        vacation.setResultVacation(nowVacation);

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


    //로그인 사용자 이름
    public EmployeeDTO showName(int employeeCode) {

        Employee result = attendanceEmployeeRepository.findByEmployeeCode(employeeCode);

        EmployeeDTO results = modelMapper.map(result, EmployeeDTO.class);

        System.out.println("============ results =========== " + results);
        return results;
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
            InsertAttendanceManagementDTO InsertAttendanceManagement = new InsertAttendanceManagementDTO();
            InsertAttendanceManagement.setAttendanceEmployeeCode(employeeCode); // 로그인한 employeeCode 정보 설정
            InsertAttendanceManagement.setAttendanceManagementArrivalTime(arrivalTime);
            InsertAttendanceManagement.setAttendanceManagementDepartureTime(departureTime);
            InsertAttendanceManagement.setAttendanceManagementState(status);
            InsertAttendanceManagement.setAttendanceManagementWorkDay(today);
            InsertAttendanceManagement.setAttendanceManagementCode(null);


            // DTO 객체를 Entity로 변환
            InsertAttendanceManagement insertAttendance = modelMapper.map(InsertAttendanceManagement, InsertAttendanceManagement.class);

            System.out.println("========= insertAttendance ======= " + insertAttendance);
            // 저장소에 저장
            insertCommuteRepository.save(insertAttendance);
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
            InsertAttendanceManagement updateAttendance = insertCommuteRepository.findFirstByAttendanceEmployeeCode_EmployeeCodeOrderByAttendanceManagementCodeDesc(employeeNum);

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
            InsertAttendanceManagement updateAttendance = insertCommuteRepository.findFirstByAttendanceEmployeeCode_EmployeeCodeOrderByAttendanceManagementCodeDesc(employeeNum);

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


    public DetailMyWaitingDTO detailMyApply(Long approvalDocumentCode) {

        System.out.println("문서 코드 = " + approvalDocumentCode);

        if(approvalDocumentCode != null) {

            //근무 형태 서류 상세 보기
            DetailMyWaing detailWaiting = detailMyRepository.findContent(approvalDocumentCode);
            System.out.println(" 상세보기 detailWaiting = " + detailWaiting);

            DetailMyWaitingDTO detailMyWaiting = modelMapper.map(detailWaiting, DetailMyWaitingDTO.class);

            return detailMyWaiting;
        }

        return null;
    }
}
