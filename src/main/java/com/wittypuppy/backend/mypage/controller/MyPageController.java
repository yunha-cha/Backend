package com.wittypuppy.backend.mypage.controller;

import com.wittypuppy.backend.Employee.dto.EmployeeDTO;
import com.wittypuppy.backend.common.dto.ResponseDTO;
import com.wittypuppy.backend.mypage.dto.MyPageEmpDTO;
import com.wittypuppy.backend.mypage.dto.MyPageUpdateDTO;
import com.wittypuppy.backend.mypage.entity.MyPageUpdateEmp;
import com.wittypuppy.backend.mypage.service.MyPageService;
import com.wittypuppy.backend.util.TokenUtils;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mypage")
@Slf4j
public class MyPageController {

    private final MyPageService myPageService;

    private final TokenUtils tokenUtils;

    public MyPageController(MyPageService myPageService, TokenUtils tokenUtils) {
        this.myPageService = myPageService;
        this.tokenUtils = tokenUtils;
    }

    @GetMapping("/emplist")
    public ResponseEntity<ResponseDTO> selectSearchMyPageEmp( @RequestParam(name = "c", defaultValue = "") Long search ){
        MyPageEmpDTO myPageEmpDTO = myPageService.selectEmpByEmpCode(search);

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "마이페에지 성공 테스트", myPageEmpDTO));

    }

//    @PutMapping("/demos/{demoCode}")
//    public ResponseEntity<ResponseDTO> updateDemo(@RequestBody @Valid DemoDTO newDemo, @PathVariable Long demoCode) {
//        log.info("DemoController >>> updateDemo >>> start");
//
//        String data = demoService.updateDemo(newDemo, demoCode);
//
//        log.info("DemoController >>> updateDemo >>> end");
//        return ResponseEntity
//                .ok()
//                .body(new ResponseDTO(HttpStatus.OK, "상품 입력 성공", data));
//    }

    @PutMapping("/modifyinfo/{empCode}")
    public ResponseEntity<ResponseDTO> modifyMyPageInfo(@RequestBody @Valid MyPageUpdateDTO myPageUpdateDTO, @PathVariable Long empCode){
        log.info("마이페이지 컨트롤러 시작");
        String data = String.valueOf(myPageService.updateMyPageByEmpCode(myPageUpdateDTO, empCode));

        log.info("마이페이지 컨트롤러 기능 끝");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "상품 수정 성공", data));

    }


    @PutMapping("/modifypwd/{empCode}")
    public ResponseEntity<ResponseDTO> resetPassword(
            @PathVariable Long empCode,
                                                     @RequestBody MyPageUpdateDTO myPageUpdateDTO,
                                                     @RequestHeader("Authorization") String authToken,
                                                     @AuthenticationPrincipal EmployeeDTO principal) {

        // principal 여기서 토큰에서 사용자 정보를 추출하여 empCode를 얻어올 수 있음
        log.info("마이페이지 바밀번호변경 컨트롤러 시작");
        System.out.println("principal = " + principal);
        Long userEmpCode = (long) principal.getEmployeeCode();

        // 토큰에 있는 empCode와 요청의 empCode를 비교하여 다른 경우 FORBIDDEN 권한 없음 응답
        if (!userEmpCode.equals(empCode)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        log.info("마이페이지 컨트롤러 비밀번호 변경 사원코드 비교",empCode,userEmpCode);
//
        try {
            MyPageUpdateDTO myPageUpdateEmp = myPageService.updateEmpPwdByEmpCode(empCode, myPageUpdateDTO.getEmpPwd(), myPageUpdateDTO.getNewEmpPwd());
            System.out.println("정보나오냐" + myPageUpdateDTO.getEmpPwd());
            System.out.println("정보나오냐" + myPageUpdateDTO.getNewEmpPwd());

            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "비밀번호 변경 성공","비밀번호 변경 성공"));
        }
       catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ResponseDTO(HttpStatus.BAD_REQUEST, e.getMessage(), null));
        }

    }





}
