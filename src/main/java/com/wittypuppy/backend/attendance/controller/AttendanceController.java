package com.wittypuppy.backend.attendance.controller;

import com.wittypuppy.backend.Employee.dto.User;
import com.wittypuppy.backend.attendance.dto.*;
import com.wittypuppy.backend.attendance.paging.Criteria;
import com.wittypuppy.backend.attendance.paging.PageDTO;
import com.wittypuppy.backend.attendance.paging.PagingResponseDTO;
import com.wittypuppy.backend.attendance.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Tag(name = "근태 스웨거 연동")
@RestController
@Slf4j
@RequestMapping("/api/v1")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }


    @Operation(summary = "근태 메인 화면 출퇴근 화면", description = "출퇴근 시간, 연차 수량, 결재을 확인할 수 있다")
    @GetMapping("/attendances/main")
    public ResponseEntity<AttendanceResponseDTO> attendanceMain(
            @AuthenticationPrincipal User employeeInFo
    ){

        int employeeCode = employeeInFo.getEmployeeCode();

        VacationDTO vacation = attendanceService.attendanceVacation(employeeCode);

        ApprovalLineDTO approvalWaiting = attendanceService.attendanceWaiting(employeeCode);

        AttendanceManagementDTO commute = attendanceService.attendanceMain(employeeCode);

        EmployeeDTO userName = attendanceService.showName(employeeCode);

        return ResponseEntity.ok().body(new AttendanceResponseDTO(HttpStatus.OK, "근태 메인 화면 조회 성공", commute, vacation, approvalWaiting, userName));
    }



    @Operation(summary = "근태 메인 화면 출근 인서트", description = "출퇴근 시간을 인서트 합니다")
    @PostMapping("/attendances/main")
    public ResponseEntity<ResponseDTO> commuteInput(
            @AuthenticationPrincipal User employeeCode,
            @RequestBody Map<String, Object> requestBody
            ){

        LocalDateTime arrivalTime = LocalDateTime.parse((String) requestBody.get("arrivalTime")); // arrivalTime은 문자열로 전송되기 때문에 파싱 필요
        String status = (boolean) requestBody.get("late") ? "지각" : "정상";

        System.out.println("====== arrivalTime ===== " + arrivalTime);
        System.out.println("======== status ======== " + status);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime departureTime = now.toLocalDate().atStartOfDay(); // 현재 날짜의 자정 시간 구하기

        String login = attendanceService.insertArrival(employeeCode, arrivalTime, departureTime, status);

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "근태 출근 등록 성공", login));
    }




    @Operation(summary = "근태 메인 화면 퇴근 수정", description = "퇴근 시간및 상태 값을 업 데이트 합니다")
    @PutMapping ("/attendances/main")
    public ResponseEntity<ResponseDTO> commuteUpdate(
            @AuthenticationPrincipal User employeeCode,
            @RequestBody Map<String, Object> requestBody
    ){

        LocalDateTime departureTime = LocalDateTime.parse((String) requestBody.get("departureTime"));
        String status = (boolean) requestBody.get("early") ? "조퇴" : "";


        if (status.equals("조퇴")) {

            String login = attendanceService.updateDeparture(employeeCode, departureTime, status);
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "근태 퇴근& 상태값 수정 성공", login));
        } else {
            String noState = attendanceService.updateOnlyDeparture(employeeCode, departureTime);
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "근태 퇴근만 수정 완료", noState));
        }

    }


//
        @Operation(summary = "근태 목록 화면 출근 리스트 확인", description = "근태 목록을 월별로 조회 합니다")
        @GetMapping("/attendances/lists")
        public ResponseEntity<WorkTypeResponseDTO> selectCommuteList(
            @RequestParam(name = "offset", defaultValue = "1") String offset,
            @RequestParam(name = "yearMonth", defaultValue = "") String yearMonth,
            @AuthenticationPrincipal User employeeInFo
        ) {

        int employeeCode = employeeInFo.getEmployeeCode();

        Criteria cri = new Criteria(Integer.valueOf(offset), 6);

        PagingResponseDTO pagingResponse = new PagingResponseDTO();

        Page<AttendanceManagementDTO> attendanceList = attendanceService.selectCommuteList(cri, yearMonth, employeeCode);
        pagingResponse.setData(attendanceList);

        pagingResponse.setPageInfo(new PageDTO(cri, (int) attendanceList.getTotalElements()));

            AttendanceManagementDTO normal = attendanceService.countNormal(employeeCode,yearMonth);

        return ResponseEntity.ok().body(new WorkTypeResponseDTO(HttpStatus.OK, "출퇴근 목록 조회 성공", pagingResponse,normal));
    }




    @Operation(summary = "내가 기안 문서", description = "내가 기안한 문서중 결재 완료된 것을 조회 합니다")
    @GetMapping("/attendances/my/documents-payment")
    public ResponseEntity<ResponseDTO> myDocumentPayment(
            @RequestParam(name = "offset", defaultValue = "1") String offset,
            @AuthenticationPrincipal User employeeInFo

    ) {

        int employeeCode = employeeInFo.getEmployeeCode();

        Criteria cri = new Criteria(Integer.valueOf(offset), 6);

        PagingResponseDTO pagingResponse = new PagingResponseDTO();

        Page<ApprovalLineDTO> myDocumentPaymentList = attendanceService.myDocumentPaymentList(cri, employeeCode);

        pagingResponse.setData(myDocumentPaymentList);
        pagingResponse.setPageInfo(new PageDTO(cri, (int) myDocumentPaymentList.getTotalElements()));

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "내 결재완료 문서 조회 성공", pagingResponse));

    }



    @Operation(summary = "내가 기안 문서", description = "내가 기안한 문서중 반려된 것을 조회 합니다")
    @GetMapping("/attendances/my/documents-companion")
    public ResponseEntity<ResponseDTO> myDocumentCompanion(
            @RequestParam(name = "offset", defaultValue = "1") String offset,
            @AuthenticationPrincipal User employeeInFo
    ) {

        int employeeCode = employeeInFo.getEmployeeCode();

        Criteria cri = new Criteria(Integer.valueOf(offset), 6);

        PagingResponseDTO pagingResponse = new PagingResponseDTO();

        Page<ApprovalLineDTO> myDocumentCompanionList = attendanceService.myDocumentCompanionList(cri, employeeCode);

        pagingResponse.setData(myDocumentCompanionList);
        pagingResponse.setPageInfo(new PageDTO(cri, (int) myDocumentCompanionList.getTotalElements()));

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "내 반려 문서 조회 성공", pagingResponse));
    }



    @Operation(summary = "내 결재 문서" , description = "내가 결재 완료한 문서를 조회 합니다")
    @GetMapping("/attendances/payment/completed")
    public ResponseEntity<ResponseDTO> PaymentCompleted (
            @RequestParam(name = "offset", defaultValue = "1") String offset,
            @AuthenticationPrincipal User employeeInFo
    ) {

        int employeeCode = employeeInFo.getEmployeeCode();

        Criteria cri = new Criteria(Integer.valueOf(offset), 6);

        PagingResponseDTO pagingResponse = new PagingResponseDTO();

        Page<ApprovalLineDTO> paymentCompletedList = attendanceService.paymentCompletedList(cri, employeeCode);

        pagingResponse.setData(paymentCompletedList);
        pagingResponse.setPageInfo(new PageDTO(cri, (int) paymentCompletedList.getTotalElements()));

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "결재한 문서 조회 성공", pagingResponse));
    }



    @Operation(summary = "내 결재 문서" , description = "내가 반려한 문서를 조회 합니다")
    @GetMapping("/attendances/payment/rejection")
    public ResponseEntity<ResponseDTO> PaymentRejection (
            @RequestParam(name = "offset", defaultValue = "1") String offset,
            @AuthenticationPrincipal User employeeInFo
    ) {

        int employeeCode = employeeInFo.getEmployeeCode();

        Criteria cri = new Criteria(Integer.valueOf(offset), 6);

        PagingResponseDTO pagingResponse = new PagingResponseDTO();

        Page<ApprovalLineDTO> paymentRejectionList = attendanceService.paymentRejectionList(cri, employeeCode);

        pagingResponse.setData(paymentRejectionList);
        pagingResponse.setPageInfo(new PageDTO(cri, (int) paymentRejectionList.getTotalElements()));

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "반려한 문서 조회 성공", pagingResponse));
    }


    @Operation(summary = "내 결재 문서" , description = "내가 결재 할 문서를 조회 합니다")
    @GetMapping("/attendances/payment/waiting")
    public ResponseEntity<ResponseDTO> PaymentWaiting (
            @RequestParam(name = "offset", defaultValue = "1") String offset,
            @AuthenticationPrincipal User employeeInFo
    ) {
        int employeeCode = employeeInFo.getEmployeeCode();

        Criteria cri = new Criteria(Integer.valueOf(offset), 6);

        PagingResponseDTO pagingResponse = new PagingResponseDTO();

        Page<ApprovalLineDTO> paymentWaitingList = attendanceService.paymentWaitingList(cri, employeeCode);

        pagingResponse.setData(paymentWaitingList);
        pagingResponse.setPageInfo(new PageDTO(cri, (int) paymentWaitingList.getTotalElements()));

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "대기 문서 조회 성공", pagingResponse));
    }




    @Operation(summary = "문서 상세 보기" , description = "문서를 상세 볼 수 있습니다")
    @GetMapping("/attendances/document/{approvalDocumentCode}")
    public ResponseEntity<WorkTypeResponseDTO> detailMyApply (
            @PathVariable Long approvalDocumentCode

    ) {

        DetailMyWaitingDTO detail = attendanceService.detailMyApply(approvalDocumentCode);

        List<ApprovalLineDTO> lineState = attendanceService.state(approvalDocumentCode);

        WorkTypeResponseDTO responseDTO = new WorkTypeResponseDTO(HttpStatus.OK, "상세 조회 성공", detail, lineState);

        return ResponseEntity.ok().body(responseDTO);
    }



    @Operation(summary = "문서 결재" , description = "상세 보기한 문서를 결재 할 수 있습니다")
    @PutMapping("/attendances/document/{approvalDocumentCode}")
    public ResponseEntity<String> approval(
            @PathVariable Long approvalDocumentCode,
            @AuthenticationPrincipal User user
    ){
        System.out.println("approvalDocCode = " + approvalDocumentCode);

        String result = attendanceService.approvalDocument(approvalDocumentCode, user);
        System.out.println("result ========== " + result);
        return ResponseEntity.ok(result);
    }




    @Operation(summary = "내가 신청한 기안 문서", description = "내가 기안한 문서중 미 결제된 것을 조회 합니다")
    @GetMapping("/attendances/my/documents-waiting")
    public ResponseEntity<ResponseDTO> myDocumentWaiting(
            @RequestParam(name = "offset", defaultValue = "1") String offset,
            @AuthenticationPrincipal User employeeInFo
    ) {

        int employeeCode = employeeInFo.getEmployeeCode();

        Criteria cri = new Criteria(Integer.valueOf(offset), 6);

        PagingResponseDTO pagingResponse = new PagingResponseDTO();

        Page<ApprovalLineDTO> myDocumentWaitingList = attendanceService.myDocumentWaitingList(cri, employeeCode);

        pagingResponse.setData(myDocumentWaitingList);
        pagingResponse.setPageInfo(new PageDTO(cri, (int) myDocumentWaitingList.getTotalElements()));

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "내 기안 문서 조회 성공", pagingResponse));

    }




}
