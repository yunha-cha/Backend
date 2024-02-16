package com.wittypuppy.backend.Employee.controller;


import com.wittypuppy.backend.Employee.dto.User;
import com.wittypuppy.backend.Employee.dto.PasswordResetRequest;
import com.wittypuppy.backend.common.dto.ResponseDTO;
import com.wittypuppy.backend.Employee.service.EmployeeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipalNotFoundException;
@Tag(name = "로그인 사원관련 스웨거 연동")
@RestController
@RequestMapping("/api/v1/employee")
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;  // service class 생성 이후 코드를 작성

    public EmployeeController(EmployeeService memberService) {
        this.employeeService = memberService;
    }

    // 조회 , /members/{memberId}
//    @ApiOperation(value = "회원 조회 요청", notes = "회원 한명이 조회됩니다.", tags = { "MemberController" })
    @Tag(name = "사원 조회" , description = "아이디를 통한 조회")
    @GetMapping("/employeeinfo/{employeeId}")
    public ResponseEntity<ResponseDTO> selectMyEmployeeInfo(@PathVariable String employeeId){

        log.info("[EmployeeController]  selectMyEmployeeInfo   Start =============== ");
        log.info("[EmployeeController]  selectMyEmployeeInfo   {} ====== ", employeeId);

        log.info("[EmployeeController]  selectMyEmployeeInfo   End ================= ");
        return ResponseEntity
                    .ok()
                    .body(new ResponseDTO(HttpStatus.OK, "조회 성공", employeeService.selectMyInfo(employeeId)));
    }

    @Tag(name = "비밀번호 찾기" , description = "아이디와 이메일을 통한 비밀번호 찾기")
    @PostMapping("/searchpwd")
    public ResponseEntity<ResponseDTO> resetPassword(@RequestBody PasswordResetRequest request) {

        System.out.println("비번 찾기 컨트롤러 시작=====================================");

        try {
            employeeService.sendSearchPwd((request.getEmployeeId()), request.getEmployeeEmail());
            System.out.println("비번 변경 컨트롤러에서 서비스부분 들어갔다 오기=====================================");

            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "임시 비밀번호로 변경 성공", "비밀번호 찾기 성공"));
        }  catch (UserPrincipalNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(HttpStatus.NOT_FOUND, "사용자 정보를 찾을 수 없습니다.", "User not found."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류 발생", "Internal server error."));
        }
    }


}
