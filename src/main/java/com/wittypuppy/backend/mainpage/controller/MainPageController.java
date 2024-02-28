package com.wittypuppy.backend.mainpage.controller;

import com.wittypuppy.backend.Employee.dto.User;
import com.wittypuppy.backend.attendance.dto.AttendanceManagementDTO;
import com.wittypuppy.backend.attendance.dto.InsertAttendanceManagementDTO;
import com.wittypuppy.backend.attendance.dto.WorkTypeResponseDTO;
import com.wittypuppy.backend.common.dto.ResponseDTO;
import com.wittypuppy.backend.mainpage.dto.MainPageBoardDTO;
import com.wittypuppy.backend.mainpage.dto.MainPageProjectListDTO;
import com.wittypuppy.backend.mainpage.service.MainPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
@Tag(name = "메인 페이지 스웨거 연동")
@RestController
@RequestMapping("/api/v1/mainpage")
public class MainPageController {

    private final MainPageService mainPageService;

    public MainPageController(MainPageService mainPageService) {
        this.mainPageService = mainPageService;
    }

    @Tag(name = "게시판 리스트 조회" , description = "메인페이지에서 게시판 조회")
    @GetMapping("/boardlist")
    public ResponseEntity<ResponseDTO> selectPostList(){
        System.out.println("메인페이지 게시판 출력 컨트롤러 시작");
        List<MainPageBoardDTO> mainPageBoardDTOList = mainPageService.selectPostList();
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"메인페이지 게시판 조회 성공", mainPageBoardDTOList));
    }

    @Tag(name = "프로젝트 조회" , description = "메인페이지에서 프로젝트 리스트 조회")
    @GetMapping("/projectlist")
    public ResponseEntity<ResponseDTO> selectProjectList(){
        System.out.println("메인페이지 프로젝트 출력 컨트롤러 시작");
        List<MainPageProjectListDTO> mainPageProjectListDTOList = mainPageService.selectProjectList();
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "메인페이지 프로젝트 조회 성공", mainPageProjectListDTOList));
    }


    
    @GetMapping("/arrive")
    public ResponseEntity<ResponseDTO> arrivedView(@AuthenticationPrincipal User user){

        int employeeCode = user.getEmployeeCode();
        AttendanceManagementDTO managementDTO =mainPageService.attendanceList(employeeCode);
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "메인페이지 근태 목록", managementDTO));
    }



    @Operation(summary = "메인홈페이지 출근 insert", description = "메인 페이지 출퇴근 인서트")
    @PostMapping("/arrive")
    public ResponseEntity<WorkTypeResponseDTO> arrivedUser(@AuthenticationPrincipal User user){
        //새로운 출퇴근 객체 생성
        InsertAttendanceManagementDTO managementDTO = new InsertAttendanceManagementDTO();
        //출근 시간을 지금으로
        managementDTO.setAttendanceManagementArrivalTime(LocalDateTime.now());
        //오전 9시를 뜻하는 LocalDateTime 객체 생성
        LocalDateTime nineAM = LocalDateTime.now().with(LocalTime.of(9, 0));
        //출근한 시간이 9시 이후니?
        if(managementDTO.getAttendanceManagementArrivalTime().isAfter(nineAM)){
            //참이면 지각
            managementDTO.setAttendanceManagementState("지각");
        }else {
            //거짓이면 정상
            managementDTO.setAttendanceManagementState("정상");
        }
        //workDay 는 지금 날짜로설정
        managementDTO.setAttendanceManagementWorkDay(LocalDate.now());

        //퇴근 시간 오늘 날짜 시작 시간
        LocalDateTime todayStart = LocalDateTime.now().with(LocalTime.MIN);
        managementDTO.setAttendanceManagementDepartureTime(todayStart);;

        //서비스로 슈웅~
        managementDTO = mainPageService.saveArrive(managementDTO,user);

        //갖다오면 보낸 객체 프론트로 보내기
        return ResponseEntity.ok().body(new WorkTypeResponseDTO(HttpStatus.OK, "메인 화면 출근 등록 성공", managementDTO, user));
    }



    @Operation(summary = "메인홈페이지 퇴근 update", description = "메인 페이지 퇴근 업데이트")
    @PutMapping("/arrive")
    public ResponseEntity<WorkTypeResponseDTO> departureUser(@AuthenticationPrincipal User user){
        //새로운 출퇴근 객체 생성
        InsertAttendanceManagementDTO managementDTO = new InsertAttendanceManagementDTO();

        //퇴근 시간을 지금으로
        managementDTO.setAttendanceManagementDepartureTime(LocalDateTime.now());

        LocalDateTime sixPM = LocalDateTime.now().with(LocalTime.of(18, 0));

        if(managementDTO.getAttendanceManagementDepartureTime().isAfter(sixPM)){

            managementDTO  = mainPageService.saveOnlyDeparture(managementDTO,user);
        }else {

            managementDTO.setAttendanceManagementState("조퇴");
            managementDTO = mainPageService.saveDeparture(managementDTO,user);
        }


        //갖다오면 보낸 객체 프론트로 보내기
        return ResponseEntity.ok().body(new WorkTypeResponseDTO(HttpStatus.OK, "메인 화면 출근 등록 성공", managementDTO, user));
    }



}
