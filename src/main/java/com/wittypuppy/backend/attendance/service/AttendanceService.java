package com.wittypuppy.backend.attendance.service;

import com.wittypuppy.backend.Employee.dto.User;
import com.wittypuppy.backend.Employee.entity.LoginEmployee;
import com.wittypuppy.backend.approval.entity.AdditionalApprovalLine;
import com.wittypuppy.backend.approval.entity.ApprovalRepresent;
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


    public AttendanceService(AttendanceEmployeeRepository attendanceEmployeeRepository, ModelMapper modelMapper, AttendanceApprovalRepository attendanceApprovalRepository, AttendanceLineRepository attendanceLineRepository, ManagementRepository managementRepository, InsertCommuteRepository insertCommuteRepository, DetailMyRepository detailMyRepository) {
        this.attendanceEmployeeRepository = attendanceEmployeeRepository;
        this.modelMapper = modelMapper;
        this.attendanceApprovalRepository = attendanceApprovalRepository;
        this.attendanceLineRepository = attendanceLineRepository;
        this.managementRepository = managementRepository;
        this.insertCommuteRepository = insertCommuteRepository;
        this.detailMyRepository = detailMyRepository;
    }

    public Page<AttendanceManagementDTO> selectCommuteList(Criteria cri, String yearMonth, int employeeCode) {

        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by(Sort.Direction.DESC, "attendance_management_work_day"));

        Page<AttendanceManagement> result = managementRepository.attendanceList(yearMonth, employeeCode, paging);

        Page<AttendanceManagementDTO> workTypeList = result.map(myDocumentWaiting -> modelMapper.map(myDocumentWaiting, AttendanceManagementDTO.class));

        return workTypeList;
    }


    public Page<ApprovalLineDTO> myDocumentWaitingList(Criteria cri, int employeeCode) {

        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by("approval_process_date").descending());

        Page<ApprovalLine> myWaiting = attendanceApprovalRepository.findByApplyDocument(employeeCode, paging);

        Page<ApprovalLineDTO> resultList = myWaiting.map(commute -> modelMapper.map(commute, ApprovalLineDTO.class));

        return resultList;
    }


    public Page<ApprovalLineDTO> myDocumentPaymentList(Criteria cri, int employeeCode) {

        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by("approval_document_code").descending());

        Page<ApprovalLine> result = attendanceApprovalRepository.findMyDocumentPayment(employeeCode, paging);

        Page<ApprovalLineDTO> resultList = result.map(myDocumentWaiting -> modelMapper.map(myDocumentWaiting, ApprovalLineDTO.class));

        return resultList;

    }


    public Page<ApprovalLineDTO> myDocumentCompanionList(Criteria cri, int employeeCode) {

        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by("approval_document_code").descending());

        Page<ApprovalLine> result = attendanceApprovalRepository.findByApprovalProcessStatusAndLineEmployeeCode_EmployeeCodeNative(paging, employeeCode);

        Page<ApprovalLineDTO> resultList = result.map(myDocumentCompanion -> modelMapper.map(myDocumentCompanion, ApprovalLineDTO.class));

        return resultList;
    }



    public Page<ApprovalLineDTO> paymentCompletedList(Criteria cri, int employeeCode) {

        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by("approval_process_date").descending());

        Page<ApprovalLine> result = attendanceApprovalRepository.approvalPayment(paging, employeeCode);

        Page<ApprovalLineDTO> resultList = result.map(paymentCompleted -> modelMapper.map(paymentCompleted, ApprovalLineDTO.class));

        return resultList;
    }


    public Page<ApprovalLineDTO> paymentRejectionList(Criteria cri, int employeeCode) {

        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by("approval_process_date").descending());

        Page<ApprovalLine> result = attendanceLineRepository.rejectionDocument(paging, employeeCode);

        Page<ApprovalLineDTO> resultList = result.map(paymentRejection -> modelMapper.map(paymentRejection, ApprovalLineDTO.class));

        return resultList;
    }

    public Page<ApprovalLineDTO> paymentWaitingList(Criteria cri, int employeeCode) {

        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by("approval_document_code").descending());

        Page<ApprovalLine> result = attendanceApprovalRepository.paymentWaiting(paging, employeeCode);

        Page<ApprovalLineDTO> resultList = result.map(paymentWaiting -> modelMapper.map(paymentWaiting, ApprovalLineDTO.class));

        return resultList;
    }


    public AttendanceManagementDTO attendanceMain(int employeeCode) {

        AttendanceManagement commute = managementRepository.attendanceCommute(employeeCode);

        if (commute == null) {

            commute = new AttendanceManagement();
            commute.setAttendanceManagementArrivalTime(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT));

        }

        AttendanceManagementDTO commutes = modelMapper.map(commute, AttendanceManagementDTO.class);

        return commutes;
    }


    public VacationDTO attendanceVacation(int employeeCode) {

        Employee vacationCount = attendanceEmployeeRepository.findByEmployeeCode(employeeCode);
        LocalDateTime join = vacationCount.getEmployeeJoinDate();

        long daysSinceJoin = Duration.between(join, LocalDateTime.now()).toDays();

        long yearsSinceJoin = daysSinceJoin / 365;

        int total = 0;

        LocalDate currentDate = LocalDate.now();

        int currentDateYear = currentDate.getYear();

        LocalDateTime futureDate = null;

        if(daysSinceJoin >= 365) {

            if(yearsSinceJoin == 1){
                total = 15;
            }else {

                total = Math.min(15 + (int) ((yearsSinceJoin - 1) / 2), 25);
            }
        } else {
            for (int i = 1; i < 12; i++) {
                futureDate = join.plusMonths(i);

                if(futureDate.isBefore(currentDate.atStartOfDay())){
                    total = i;
                }
            }
        }

        Long useVacation = managementRepository.attendanceUseVacation(employeeCode);
        Long useHalfVacation = managementRepository.attendanceUseHalfVacation(employeeCode);

        int usedVacationDays = useVacation.intValue();
        int usedHalfVacationDays = useHalfVacation.intValue();

        double nowVacation =0;

        LocalDateTime marchFirst = currentDate.withMonth(3).withDayOfMonth(1).atStartOfDay().minusDays(1);


        if (currentDate.isBefore(ChronoLocalDate.from(marchFirst)) ){

            if (futureDate.getYear() < currentDateYear) {
                double vacationDay = total - usedVacationDays - (usedHalfVacationDays * 0.5);
                nowVacation = vacationDay + total;
            } else if (futureDate.getYear() == currentDateYear) {
                nowVacation = total - usedVacationDays - (usedHalfVacationDays * 0.5);
            }

        } else if (currentDate.isAfter(ChronoLocalDate.from(marchFirst))) {

            nowVacation = total - usedVacationDays - (usedHalfVacationDays * 0.5);
        }


        VacationDTO vacation = new VacationDTO();
        vacation.setTotal(total);
        vacation.setUseVacation(usedVacationDays);
        vacation.setUseHalfVacation(usedHalfVacationDays);
        vacation.setResultVacation(nowVacation);

        return vacation;

    }


    public ApprovalLineDTO attendanceWaiting(int employeeCode) {

        List<ApprovalLine> results = attendanceApprovalRepository.attendanceWaiting(employeeCode);

        int waitingCount = 0;

        for (ApprovalLine approvalLine : results) {
            if (approvalLine != null && "대기".equals(approvalLine.getApprovalProcessStatus())) {
                waitingCount++;
            }
        }

        ApprovalLineDTO dto = new ApprovalLineDTO();
        dto.setCountWaiting(waitingCount);

        return dto;

    }


    public EmployeeDTO showName(int employeeCode) {

        Employee result = attendanceEmployeeRepository.findByEmployeeCode(employeeCode);

        EmployeeDTO results = modelMapper.map(result, EmployeeDTO.class);

        return results;
    }


    @Transactional
    public String insertArrival(User employeeCode, LocalDateTime arrivalTime, LocalDateTime departureTime, String status) {

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeCode(employeeCode.getEmployeeCode());
        int result = 0;

        try {
            LocalDate today = LocalDate.now();

            InsertAttendanceManagementDTO InsertAttendanceManagement = new InsertAttendanceManagementDTO();
            InsertAttendanceManagement.setAttendanceEmployeeCode(employeeDTO);
            InsertAttendanceManagement.setAttendanceManagementArrivalTime(arrivalTime);
            InsertAttendanceManagement.setAttendanceManagementDepartureTime(departureTime);
            InsertAttendanceManagement.setAttendanceManagementState(status);
            InsertAttendanceManagement.setAttendanceManagementWorkDay(today);
            InsertAttendanceManagement.setAttendanceManagementCode(null);

            InsertAttendanceManagement insertAttendance = modelMapper.map(InsertAttendanceManagement, InsertAttendanceManagement.class);

            System.out.println("========= insertAttendance ======= " + insertAttendance);

            insertCommuteRepository.save(insertAttendance);
            result = 1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result > 0 ? "출근 인서트 성공" : "출근 인서트 실패";
    }





    @Transactional
    public String updateDeparture(User employeeCode, LocalDateTime departureTime, String status) {


        int employeeNum = employeeCode.getEmployeeCode();

        int result = 0;

        try {

            InsertAttendanceManagement updateAttendance = insertCommuteRepository.findFirstByAttendanceEmployeeCode_EmployeeCodeOrderByAttendanceManagementCodeDesc(employeeNum);

            updateAttendance.setAttendanceManagementDepartureTime(departureTime);
            updateAttendance.setAttendanceManagementState(status);

            result = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result > 0 ? "퇴근시간 수정 성공" : "퇴근시간  수정 실패";
    }




    @Transactional
    public String updateOnlyDeparture(User employeeCode, LocalDateTime departureTime) {


        int employeeNum = employeeCode.getEmployeeCode();

        int result = 0;

        try {

            InsertAttendanceManagement updateAttendance = insertCommuteRepository.findFirstByAttendanceEmployeeCode_EmployeeCodeOrderByAttendanceManagementCodeDesc(employeeNum);

            updateAttendance.setAttendanceManagementDepartureTime(departureTime);

            result = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result > 0 ? "퇴근시간 수정 성공" : "퇴근시간  수정 실패";

    }




    public AttendanceManagementDTO countNormal(int employeeCode, String yearMonth) {

        List<AttendanceManagement> attendanceList = managementRepository.normal(employeeCode, yearMonth);

        int normalCount = 0;
        int lateCount = 0;
        int earlyCount = 0;

        for (AttendanceManagement attendance : attendanceList) {
            String state = attendance.getAttendanceManagementState();

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
                    break;
            }
        }

        AttendanceManagementDTO attendanceManagementDTO = new AttendanceManagementDTO();
        attendanceManagementDTO.setNormal(normalCount);
        attendanceManagementDTO.setLate(lateCount);
        attendanceManagementDTO.setEarly(earlyCount);


        return attendanceManagementDTO;
    }


    public DetailMyWaitingDTO detailMyApply(Long approvalDocumentCode) {

        if(approvalDocumentCode != null) {

            DetailMyWaing detailWaiting = detailMyRepository.findContent(approvalDocumentCode);

            DetailMyWaitingDTO detailMyWaiting = modelMapper.map(detailWaiting, DetailMyWaitingDTO.class);

            return detailMyWaiting;
        }
        return null;
    }



    public List<ApprovalLineDTO> state(Long approvalDocumentCode) {

        if(approvalDocumentCode != null) {

            List<ApprovalLine> lineState = attendanceApprovalRepository.findByApprovalDocumentCode(approvalDocumentCode);
            System.out.println(" 상태 보기 lineState = " + lineState);

            List<ApprovalLineDTO> lineStates = modelMapper.map(lineState, List.class);

            return lineStates;
        }
        return null;
    }


    @Transactional
    public String approvalDocument(Long approvalDocumentCode, User user) {

        // 결재 대상 조회
        Integer approvalSubject = attendanceApprovalRepository.approvalSubjectEmployeeCode(approvalDocumentCode);

        // 로그인한 사용자의 정보 가져오기
        int employeeCode = user.getEmployeeCode();


        if (approvalSubject != null && approvalSubject == employeeCode) {
            ApprovalLine updateProcess = attendanceApprovalRepository.approvalList(approvalDocumentCode, employeeCode);
            updateProcess.setApprovalProcessDate(LocalDateTime.now());
            updateProcess.setApprovalProcessStatus("결재");
            attendanceApprovalRepository.save(updateProcess);
            return "결재 성공";
        } else if (approvalSubject == null) {
            System.out.println("이미 결재 했습니다");
        }

        return "결재 대상이 아닙니다.";

    }



}
