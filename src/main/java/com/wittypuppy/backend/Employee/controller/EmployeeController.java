package com.wittypuppy.backend.Employee.controller;


import com.wittypuppy.backend.Employee.dto.EmployeeDTO;
import com.wittypuppy.backend.Employee.dto.PasswordResetRequest;
import com.wittypuppy.backend.common.dto.ResponseDTO;
import com.wittypuppy.backend.Employee.service.EmployeeService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipalNotFoundException;

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
    @GetMapping("/employeeinfo/{employeeId}")
    public ResponseEntity<ResponseDTO> selectMyEmployeeInfo(@PathVariable String employeeId){

        log.info("[EmployeeController]  selectMyEmployeeInfo   Start =============== ");
        log.info("[EmployeeController]  selectMyEmployeeInfo   {} ====== ", employeeId);

        log.info("[EmployeeController]  selectMyEmployeeInfo   End ================= ");
        return ResponseEntity
                    .ok()
                    .body(new ResponseDTO(HttpStatus.OK, "조회 성공", employeeService.selectMyInfo(employeeId)));
    }


    @PostMapping("/searchpwd")
    public ResponseEntity<ResponseDTO> resetPassword(@RequestBody PasswordResetRequest request,@AuthenticationPrincipal EmployeeDTO principal) {

        System.out.println("비번 변경 컨트롤러 시작=====================================");
//        request.setEmployeeId(principal.getEmployeeId());
//        request.setEmployeeEmail(principal.getEmployeeEmail());

        try {
            employeeService.sendSearchPwd((request.getEmployeeId()), request.getEmployeeEmail());
            System.out.println("비번 변경 컨트롤러에서 서비스부분 들어갔다 오기=====================================");

            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "임시 비밀번호로 변경 성공", "변경 성공"));
        }  catch (UserPrincipalNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(HttpStatus.NOT_FOUND, "사용자 정보를 찾을 수 없습니다.", "User not found."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류 발생", "Internal server error."));
        }
    }




//    @PostMapping("/logout")
//    public ResponseEntity<ResponseDTO> logout(HttpServletRequest request, HttpServletResponse response) {
//        // 로그아웃 처리 로직
//        // 여기에서는 세션 무효화 및 토큰 무효화 등을 수행할 수 있습니다.
//
//        // 세션 무효화 예제 (Spring Security를 사용하는 경우)
//        SecurityContextHolder.clearContext();
//
//        // 세션 무효화 및 쿠키 삭제 예제
//        HttpSession session = request.getSession(false);
//        if (session != null) {
//            session.invalidate();
//        }
//
//        // 클라이언트에게 쿠키 삭제 요청
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                cookie.setMaxAge(0);
//                cookie.setPath("/");
//                response.addCookie(cookie);
//            }
//        }
//
//        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "로그아웃 성공", "로그아웃 완료"));
//    }


}
