package com.wittypuppy.backend.attendance.controller;


import com.wittypuppy.backend.attendance.dto.AttendanceManagementAndWorkTypeDTO;
import com.wittypuppy.backend.attendance.paging.Criteria;
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



@RestController
@Slf4j
@RequestMapping("/api/v1")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }



    @GetMapping("/attendances/list")
    public ResponseEntity<ResponseDTO> selectCommuteListWithPaging (
            @RequestParam(name = "offset", defaultValue = "1") String offset) {

        System.out.println("==============startCommuteList==================");
        System.out.println("offset =========================== " + offset);

        Criteria cri = new Criteria(Integer.valueOf(offset),6);

        PagingResponseDTO pagingResponse = new PagingResponseDTO();

        Page<AttendanceManagementAndWorkTypeDTO>attendanceAndWorkTypeList = attendanceService.selectCommuteListWithPaging(cri);



        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "",pagingResponse));
    }



}
