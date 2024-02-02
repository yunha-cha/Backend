package com.wittypuppy.backend.attendance.controller;


import com.wittypuppy.backend.attendance.dto.ApprovalDocumentDTO;
import com.wittypuppy.backend.attendance.dto.ApprovalLineDTO;
import com.wittypuppy.backend.attendance.dto.AttendanceWorkTypeDTO;
import com.wittypuppy.backend.attendance.entity.ApprovalLine;
import com.wittypuppy.backend.attendance.paging.Criteria;
import com.wittypuppy.backend.attendance.paging.PageDTO;
import com.wittypuppy.backend.attendance.paging.PagingResponseDTO;
import com.wittypuppy.backend.attendance.service.AttendanceService;
import com.wittypuppy.backend.common.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@Slf4j
@RequestMapping("/api/v1")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }


    //근태 메인화면
    @GetMapping("/attendances/main")
    public ResponseEntity<ResponseDTO> attendanceMain(
            //출근 시간 받아오기
            //퇴근시간 받아오기
            //날짜 받아오기
            //로그인 값 받아오기
            ){
        Long employeeCode = 1L; // 로그인한 코드 넣기

        /*
        * 출퇴근 날짜 인서트 서류 참고로 해서
        * 남은 연차 보여주기
        * 결재 대기건 보여주기
        * */
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "상품 상세정보 조회 성공",  attendanceService.attendaceMain(employeeCode)));

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
