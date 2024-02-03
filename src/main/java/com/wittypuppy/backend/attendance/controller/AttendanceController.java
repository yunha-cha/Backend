package com.wittypuppy.backend.attendance.controller;


import com.wittypuppy.backend.attendance.dto.*;
import com.wittypuppy.backend.attendance.paging.Criteria;
import com.wittypuppy.backend.attendance.paging.PageDTO;
import com.wittypuppy.backend.attendance.paging.PagingResponseDTO;
import com.wittypuppy.backend.attendance.service.AttendanceService;
import com.wittypuppy.backend.common.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@Slf4j
@RequestMapping("/api/v1")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }


    //근태 메인화면 출근 완료 화면 or 퇴근 완료 화면
    @GetMapping("/attendances/main")
    public ResponseEntity<AttendanceResponseDTO> attendanceMain(){

        Long employeeCode = 1L; // 로그인한 코드 넣기

        System.out.println("========== employeeCode =========> " + employeeCode);
        System.out.println("=========== attendanceMainControllerStart ============");

        /*
        * 남은 연차 보여주기 (연차 tbl_vacation (연차 타입 연차, 반차 인지/ 사용여부가 N / 만료 일자전 까지))
        *
        * 결재 대기건 보여주기 수량 ->
        * 오늘 출근한 시간 보여주기 )
        * 오늘 퇴근한 시간 보여주기
        *
        * */

        // 오늘 출퇴근 보여 주기
        AttendanceManagementDTO commute = attendanceService.attendanceMain(employeeCode);

        //남은 연차 수량
        VacationDTO vacation = attendanceService.attendanceVacation(employeeCode);

        //결재 대기 수량 보여 주기
        ApprovalLineDTO approvalWaiting = attendanceService.attendanceWaiting(employeeCode);

        return ResponseEntity.ok().body(new AttendanceResponseDTO(HttpStatus.OK, "근태 메인 화면 조회 성공", commute, vacation, approvalWaiting));

    }

    @PostMapping("/attendances/main")
    public ResponseEntity<AttendanceResponseDTO> commuteInput(
            @RequestParam(name = "arrival", defaultValue = "2024-02-03 08:57:33") LocalDateTime arrival

    ){

        Long employeeCode = 1L; // 로그인한 코드 넣기

        System.out.println("========== employeeCode =========> " + employeeCode);
        System.out.println("=========== commuteInput ControllerStart ============");

        /*
        * 출근, 퇴근 시간 인서트 -> 퇴근시간은 업데이트(직원코드기준 출근시간이 마지막인거에 퇴근 업데이트 )
        * 공휴일, 주말 제외 조건
        *
        *
        * 결재선의 상태에서 마지막 단계 까지 승인된(결재)것이 없는 경우
        * -> 출근 9시 지나면 지각, 9까지 오면 정상:
        * -> 18:00:00 까지 출근 찍히지 않은 경우 결근으로 인서트
         *
        *
        * 결재선의 상태에서 마지막 단계 까지 승인된(결재)것 ->  결재문서 양식 (SW사용신청서 제외한 모든 양식 ( 근태신청서- 외근,출장) ) :
        * -> 자동으로 출근 퇴근 시간 기입 하는 경우 (출근시간에 외근, 퇴근 시간에 외근, 출장) :오늘 날짜가 신청한 날짜와 동일하면 자동으로 넣기 쿼리 조건문
        * -> 출근 시간, 퇴근 시간 " 00:00:00 "  자동 표기할 경우 ( 연차, ..휴가 )
        * -> 퇴근시간 연장근무 시간 자동 퇴근 기록, 출장 (오늘 날짜와 출근 날짜가 동일하면 18:00:00 자동 퇴근 조건 쿼리 부여)
        * ->/
        * tbl_attendance_management , tbl_attendance_work_type, tbl_approval_document, tbl_approval_line, tbl_work_type, tbl_overwork, tbl_on_leave
        * */

        return null;
    }






        //출퇴근 목록
    @GetMapping("/attendances/lists")
    public ResponseEntity<ResponseDTO> selectCommuteList(
            @RequestParam(name = "offset", defaultValue = "1") String offset,
            @RequestParam(name = "yearMonth", defaultValue = "2023-12") String yearMonth  //리액트 값 받기

    ) {
        System.out.println("==============selectCommuteList==================");
        System.out.println("===============offset ================= " + offset);
        System.out.println("===============year ================= " + yearMonth);


        Long employeeCode = 1L; // 로그인한 코드 넣기

        Criteria cri = new Criteria(Integer.valueOf(offset), 6);

        PagingResponseDTO pagingResponse = new PagingResponseDTO();

        Page<AttendanceWorkTypeDTO> attendanceList = attendanceService.selectCommuteList(cri, yearMonth, employeeCode);
        pagingResponse.setData(attendanceList);
        pagingResponse.setPageInfo(new PageDTO(cri, (int) attendanceList.getTotalElements()));

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "출퇴근 목록 조회 성공", pagingResponse));
    }



    //내가 신청한 문서 기안
    @GetMapping("/attendances/my/documents-waiting")
    public ResponseEntity<ResponseDTO> myDocumentWaiting(
            @RequestParam(name = "offset", defaultValue = "1") String offset

            // @AuthenticationPrincipal 로그인 한 DTO
        ) {

        System.out.println("====controller======documentWaitingStart==========");
        System.out.println("========== offset ======== " + offset);

        Long employeeCode = 1L; // 로그인한 코드 넣기

        Criteria cri = new Criteria(Integer.valueOf(offset), 6);

        PagingResponseDTO pagingResponse = new PagingResponseDTO();

        Page<ApprovalLineDTO> myDocumentWaitingList = attendanceService.myDocumentWaitingList(cri, employeeCode);


        pagingResponse.setData(myDocumentWaitingList);
        pagingResponse.setPageInfo(new PageDTO(cri, (int) myDocumentWaitingList.getTotalElements()));

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "내 기안 문서 조회 성공", pagingResponse));

    }


    //내가 신청한 문서 결재 완료
    @GetMapping("/attendances/my/documents-payment")
    public ResponseEntity<ResponseDTO> myDocumentPayment(
            @RequestParam(name = "offset", defaultValue = "1") String offset

            // @AuthenticationPrincipal 로그인 한 DTO
    ) {

        System.out.println("====controller======myDocumentPayment==========");
        System.out.println("========== offset ======== " + offset);

        Long employeeCode = 1L; // 로그인한 코드 넣기

        Criteria cri = new Criteria(Integer.valueOf(offset), 6);

        PagingResponseDTO pagingResponse = new PagingResponseDTO();

        Page<ApprovalLineDTO> myDocumentPaymentList = attendanceService.myDocumentPaymentList(cri, employeeCode);


        pagingResponse.setData(myDocumentPaymentList);
        pagingResponse.setPageInfo(new PageDTO(cri, (int) myDocumentPaymentList.getTotalElements()));

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "내 결재완료 문서 조회 성공", pagingResponse));

    }


    //내가 신청한 문서 반려
    @GetMapping("/attendances/my/documents-companion")
    public ResponseEntity<ResponseDTO> myDocumentCompanion(
            @RequestParam(name = "offset", defaultValue = "1") String offset

            // @AuthenticationPrincipal 로그인 한 DTO
    ) {
        System.out.println("====controller======myDocumentCompanion==========");
        System.out.println("========== offset ======== " + offset);

        Long employeeCode = 1L; // 로그인한 코드 넣기

        Criteria cri = new Criteria(Integer.valueOf(offset), 6);

        PagingResponseDTO pagingResponse = new PagingResponseDTO();

        Page<ApprovalLineDTO> myDocumentCompanionList = attendanceService.myDocumentCompanionList(cri, employeeCode);


        pagingResponse.setData(myDocumentCompanionList);
        pagingResponse.setPageInfo(new PageDTO(cri, (int) myDocumentCompanionList.getTotalElements()));

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "내 반려 문서 조회 성공", pagingResponse));
    }


    //내가 결재한 문서- 결재완료
    @GetMapping("/attendances/payment/completed")
    public ResponseEntity<ResponseDTO> PaymentCompleted (
            @RequestParam(name = "offset", defaultValue = "1") String offset
            // @AuthenticationPrincipal 로그인 한 DTO
    ) {
        System.out.println("====controller======PaymentCompleted==========");
        System.out.println("========== offset ======== " + offset);

        Long employeeCode = 1L; // 로그인한 코드 넣기

        Criteria cri = new Criteria(Integer.valueOf(offset), 6);

        PagingResponseDTO pagingResponse = new PagingResponseDTO();

        Page<ApprovalLineDTO> paymentCompletedList = attendanceService.paymentCompletedList(cri, employeeCode);


        pagingResponse.setData(paymentCompletedList);
        pagingResponse.setPageInfo(new PageDTO(cri, (int) paymentCompletedList.getTotalElements()));

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "결재한 문서 조회 성공", pagingResponse));
    }


    //내가 결재한 문서- 반려함
    @GetMapping("/attendances/payment/rejection")
    public ResponseEntity<ResponseDTO> PaymentRejection (
            @RequestParam(name = "offset", defaultValue = "1") String offset
            // @AuthenticationPrincipal 로그인 한 DTO
    ) {
        System.out.println("====controller======PaymentRejection==========");
        System.out.println("========== offset ======== " + offset);

        Long employeeCode = 3L; // 로그인한 코드 넣기

        Criteria cri = new Criteria(Integer.valueOf(offset), 6);

        PagingResponseDTO pagingResponse = new PagingResponseDTO();

        Page<ApprovalLineDTO> paymentRejectionList = attendanceService.paymentRejectionList(cri, employeeCode);


        pagingResponse.setData(paymentRejectionList);
        pagingResponse.setPageInfo(new PageDTO(cri, (int) paymentRejectionList.getTotalElements()));

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "반려한 문서 조회 성공", pagingResponse));
    }


    //결재할 문서 -대기
    @GetMapping("/attendances/payment/waiting")
    public ResponseEntity<ResponseDTO> PaymentWaiting (
            @RequestParam(name = "offset", defaultValue = "1") String offset
            // @AuthenticationPrincipal 로그인 한 DTO
    ) {
        System.out.println("====controller======PaymentWaiting==========");
        System.out.println("========== offset ======== " + offset);

        Long employeeCode = 2L; // 로그인한 코드 넣기

        Criteria cri = new Criteria(Integer.valueOf(offset), 6);

        PagingResponseDTO pagingResponse = new PagingResponseDTO();

        Page<ApprovalLineDTO> paymentWaitingList = attendanceService.paymentWaitingList(cri, employeeCode);


        pagingResponse.setData(paymentWaitingList);
        pagingResponse.setPageInfo(new PageDTO(cri, (int) paymentWaitingList.getTotalElements()));

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "대기 문서 조회 성공", pagingResponse));
    }
}
