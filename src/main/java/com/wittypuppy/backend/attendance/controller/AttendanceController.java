package com.wittypuppy.backend.attendance.controller;


import com.wittypuppy.backend.attendance.dto.*;
import com.wittypuppy.backend.attendance.paging.Criteria;
import com.wittypuppy.backend.attendance.paging.PageDTO;
import com.wittypuppy.backend.attendance.paging.PagingResponseDTO;
import com.wittypuppy.backend.attendance.service.AttendanceService;
import com.wittypuppy.backend.common.dto.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;

@Tag(name = "근태 스웨거 연동")
@RestController
@Slf4j
@RequestMapping("/api/v1")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }


    //근태 메인화면 출근 완료 화면 or 퇴근 완료 화면
    @Operation(summary = "근태 메인 화면 출퇴근 화면", description = "출퇴근 시간, 연차 수량을 확인할 수 있다")
    @GetMapping("/attendances/main")
    public ResponseEntity<AttendanceResponseDTO> attendanceMain(){

        Long employeeCode = 1L; // 로그인한 코드 넣기

        System.out.println("========== employeeCode =========> " + employeeCode);
        System.out.println("=========== attendanceMainControllerStart ============");

        /*
        * 남은 연차 보여주기 (연차 tbl_vacation (연차 타입 연차, 반차 인지/ 사용여부가 N / 만료 일자전 까지))
        *
        * 결재 대기건 보여주기 수량 -->
        * 오늘 출근한 시간 보여주기 )
        * 오늘 퇴근한 시간 보여주기
        *
        * */


        //남은 연차 수량
        VacationDTO vacation = attendanceService.attendanceVacation(employeeCode);

        //결재 대기 수량 보여 주기
        ApprovalLineDTO approvalWaiting = attendanceService.attendanceWaiting(employeeCode);

        // 오늘 출퇴근 보여 주기
        AttendanceManagementDTO commute = attendanceService.attendanceMain(employeeCode);


        return ResponseEntity.ok().body(new AttendanceResponseDTO(HttpStatus.OK, "근태 메인 화면 조회 성공", commute, vacation, approvalWaiting));

    }


    @Operation(summary = "근태 메인 화면 출근 인서트", description = "출퇴근 시간을 인서트 합니다")
    @PostMapping("/attendances/main")
    public ResponseEntity<ResponseDTO> commuteInput(
            @RequestParam(name = "arrivalTime", defaultValue = "") String arrivalTime,
            @RequestParam(name = "departureTime", defaultValue = "00:00:00") String departureTime,
            @RequestParam(name = "late", defaultValue = "false") boolean late
            ){

        Long employeeCode = 2L; // 로그인한 코드 넣기

        System.out.println("========== employeeCode =========> " + employeeCode);
        System.out.println("=========== commuteInput ControllerStart ============");
        System.out.println("==== arrivalTime ======= " + arrivalTime);
        System.out.println("====== departureTime ====== " + departureTime);
        System.out.println("====== late ====== " + late);

        /*
        * 출근, 퇴근 시간 인서트 -> 퇴근시간은 업데이트(직원코드기준 출근시간이 마지막인거에 퇴근 업데이트 )
        *
        *로그인 하면 출근을 찍고 -> 출근 정보를 인서트 (퇴근 00:00:00) 같이 인서트
        * -> 오늘 날짜 출근 9시 지나면 지각, 9까지 오면 정상:래액트 기능으로 조건 설정
        *
        * 퇴근 업데이트 -> 가장 최근 출근 목록에서 업데이트 (로그인 정보 동일) //
* */

        // 로그인 해서 출근 인서트
        String login = attendanceService.insertArrival(employeeCode, arrivalTime, departureTime, late);

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "근태 출근 등록 성공", login));
    }

    //퇴근 시간 업데이트

    @Operation(summary = "근태 메인 화면 퇴근 수정", description = "퇴근 시간을 업데이트 합니다")
    @PutMapping ("/attendances/main")
    public ResponseEntity<ResponseDTO> commuteUpdate(
            @RequestBody AttendanceManagementDTO attendanceManagementDTO,
            @RequestParam(name = "departure", defaultValue = "") DateTimeFormat departure

    ){

        Long employeeCode = 1L; // 로그인한 코드 넣기

        System.out.println("========== employeeCode ==========> " + employeeCode);
        System.out.println("========== departure =========== " + departure);
        System.out.println("=========== commuteUpdate ControllerStart ============");

        System.out.println(attendanceManagementDTO.getAttendanceManagementDepartureTime());

        // 퇴근 업데이트
        String login = attendanceService.updateDeparture(employeeCode, attendanceManagementDTO);

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "근태 퇴근 수정 성공", login));
    }



        //출퇴근 목록
        @Operation(summary = "근태 목록 화면 출근 리스트 확인", description = "근태 목록을 월별로 조회 합니다")
    @GetMapping("/attendances/lists")
    public ResponseEntity<ResponseDTO> selectCommuteList(
            @RequestParam(name = "offset", defaultValue = "1") String offset,
            @RequestParam(name = "yearMonth", defaultValue = "") String yearMonth  //리액트 값 받기

    ) {
        System.out.println("==============selectCommuteList==================");
        System.out.println("===============offset ================= " + offset);
        System.out.println("===============year ================= " + yearMonth);


        Long employeeCode = 1L; // 로그인한 코드 넣기

        Criteria cri = new Criteria(Integer.valueOf(offset), 6);

        PagingResponseDTO pagingResponse = new PagingResponseDTO();

        Page<AttendanceWorkTypeDTO> attendanceList = attendanceService.selectCommuteList(cri, yearMonth, employeeCode);
        pagingResponse.setData(attendanceList);

        System.out.println("==================attendanceList = " + attendanceList);
        pagingResponse.setPageInfo(new PageDTO(cri, (int) attendanceList.getTotalElements()));

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "출퇴근 목록 조회 성공", pagingResponse));
    }



    //내가 신청한 문서 기안

    @Operation(summary = "내가 기안 문서", description = "내가 기안한 문서중 미 결제된 것을 조회 합니다")
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
    @Operation(summary = "내가 기안 문서", description = "내가 기안한 문서중 결재 완료된 것을 조회 합니다")
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
    @Operation(summary = "내가 기안 문서", description = "내가 기안한 문서중 반려된 것을 조회 합니다")
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
    @Operation(summary = "내 결재 문서" , description = "내가 결재 완료한 문서를 조회 합니다")
    @GetMapping("/attendances/payment/completed")
    public ResponseEntity<ResponseDTO> PaymentCompleted (
            @RequestParam(name = "offset", defaultValue = "1") String offset
            // @AuthenticationPrincipal 로그인 한 DTO
    ) {
        System.out.println("====controller======PaymentCompleted==========");
        System.out.println("========== offset ======== " + offset);

        Long employeeCode = 31L; // 로그인한 코드 넣기

        Criteria cri = new Criteria(Integer.valueOf(offset), 6);

        PagingResponseDTO pagingResponse = new PagingResponseDTO();

        Page<ApprovalLineDTO> paymentCompletedList = attendanceService.paymentCompletedList(cri, employeeCode);


        pagingResponse.setData(paymentCompletedList);
        pagingResponse.setPageInfo(new PageDTO(cri, (int) paymentCompletedList.getTotalElements()));

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "결재한 문서 조회 성공", pagingResponse));
    }


    //내가 결재한 문서- 반려함
    @Operation(summary = "내 결재 문서" , description = "내가 반려한 문서를 조회 합니다")
    @GetMapping("/attendances/payment/rejection")
    public ResponseEntity<ResponseDTO> PaymentRejection (
            @RequestParam(name = "offset", defaultValue = "1") String offset
            // @AuthenticationPrincipal 로그인 한 DTO
    ) {
        System.out.println("====controller======PaymentRejection==========");
        System.out.println("========== offset ======== " + offset);

        Long employeeCode = 31L; // 로그인한 코드 넣기

        Criteria cri = new Criteria(Integer.valueOf(offset), 6);

        PagingResponseDTO pagingResponse = new PagingResponseDTO();

        Page<ApprovalLineDTO> paymentRejectionList = attendanceService.paymentRejectionList(cri, employeeCode);


        pagingResponse.setData(paymentRejectionList);
        pagingResponse.setPageInfo(new PageDTO(cri, (int) paymentRejectionList.getTotalElements()));

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "반려한 문서 조회 성공", pagingResponse));
    }


    //결재할 문서 -대기
    @Operation(summary = "내 결재 문서" , description = "내가 결재 할 문서를 조회 합니다")
    @GetMapping("/attendances/payment/waiting")
    public ResponseEntity<ResponseDTO> PaymentWaiting (
            @RequestParam(name = "offset", defaultValue = "1") String offset
            // @AuthenticationPrincipal 로그인 한 DTO
    ) {
        System.out.println("====controller======PaymentWaiting==========");
        System.out.println("========== offset ======== " + offset);

        Long employeeCode = 31L; // 로그인한 코드 넣기

        Criteria cri = new Criteria(Integer.valueOf(offset), 6);

        PagingResponseDTO pagingResponse = new PagingResponseDTO();

        Page<ApprovalLineDTO> paymentWaitingList = attendanceService.paymentWaitingList(cri, employeeCode);


        pagingResponse.setData(paymentWaitingList);
        pagingResponse.setPageInfo(new PageDTO(cri, (int) paymentWaitingList.getTotalElements()));

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "대기 문서 조회 성공", pagingResponse));
    }
}
