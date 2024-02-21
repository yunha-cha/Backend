package com.wittypuppy.backend.attendance.adminAttend;

import com.wittypuppy.backend.Employee.dto.User;
import com.wittypuppy.backend.attendance.dto.EmployeeDTO;
import com.wittypuppy.backend.attendance.dto.ResponseDTO;
import com.wittypuppy.backend.attendance.paging.Criteria;
import com.wittypuppy.backend.attendance.paging.PageDTO;
import com.wittypuppy.backend.attendance.paging.PagingResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@RequestMapping("/api/v1")
public class AttendAdminController {

    private final AttendanceAdminService attendanceAdminService;

    public AttendAdminController(AttendanceAdminService attendanceAdminService) {
        this.attendanceAdminService = attendanceAdminService;
    }

    //관리자 근태 화면 연차
    @GetMapping("/attendances/admin/vacation")
    public ResponseEntity<ResponseDTO> mainVacation(
            @RequestParam(name = "offset", defaultValue = "1") String offset,
            @AuthenticationPrincipal User employeeCode
    ){
        System.out.println("========== offset ======== " + offset);

        System.out.println("=======관리자 연차 목록 ===== ");

        Criteria cri = new Criteria(Integer.valueOf(offset), 6);

        PagingResponseDTO pagingResponse = new PagingResponseDTO();

        Page<AdminEmployeeDTO> employeeVacationList = attendanceAdminService.mainVacation(cri);
        pagingResponse.setData(employeeVacationList);

        System.out.println("==================employeeVacationList = " + employeeVacationList);
        pagingResponse.setPageInfo(new PageDTO(cri, (int) employeeVacationList.getTotalElements()));

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "관리자 연차 조회 성공", pagingResponse));
    }


    @GetMapping("/attendances/admin/no/vacation")
    public ResponseEntity<ResponseDTO> noVacation(
            @RequestParam(name = "offset", defaultValue = "1") String offset,
            @AuthenticationPrincipal User employeeCode
    ){
        System.out.println("========== offset ======== " + offset);

        System.out.println("=======관리자 미할당 연차 목록 ===== ");

        Criteria cri = new Criteria(Integer.valueOf(offset), 6);

        PagingResponseDTO pagingResponse = new PagingResponseDTO();

        Page<AdminEmployeeDTO> employeeNoVacationList = attendanceAdminService.noVacation(cri);
        pagingResponse.setData(employeeNoVacationList);

        System.out.println("==================employeeVacationList = " + employeeNoVacationList);
        pagingResponse.setPageInfo(new PageDTO(cri, (int) employeeNoVacationList.getTotalElements()));

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "관리자 미할당 성공", pagingResponse));
    }


}
