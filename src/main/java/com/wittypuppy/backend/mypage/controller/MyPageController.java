package com.wittypuppy.backend.mypage.controller;

import com.wittypuppy.backend.Employee.dto.User;
import com.wittypuppy.backend.common.dto.ResponseDTO;
import com.wittypuppy.backend.mypage.dto.MyPageEmpDTO;
import com.wittypuppy.backend.mypage.dto.MyPageUpdateDTO;
import com.wittypuppy.backend.mypage.service.MyPageService;
import com.wittypuppy.backend.util.TokenUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "마이페이지 스웨거 연동")
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

    @Tag(name = "내정보 조회" , description = "마이페이지에서 사원번호로 조회")
    @GetMapping("/emplist")
    public ResponseEntity<ResponseDTO> selectSearchMyPageEmp(
            @RequestParam(name = "c", defaultValue = "") Long search ){
        MyPageEmpDTO myPageEmpDTO = myPageService.selectEmpByEmpCode(search);


        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "마이페에지 사원번호 조회 성공 ", myPageEmpDTO));

    }

    @Tag(name = "내 정보 수정" , description = "마이페이지에서 내 정보 수정")
    @PutMapping("/modifyinfo/{empCode}")
    public ResponseEntity<ResponseDTO> modifyMyPageInfo(@RequestBody @Valid MyPageUpdateDTO myPageUpdateDTO, @PathVariable Long empCode){
        log.info("마이페이지 컨트롤러 시작");
        String data = String.valueOf(myPageService.updateMyPageByEmpCode(myPageUpdateDTO, empCode));

        log.info("마이페이지 컨트롤러 기능 끝");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "내 정보 수정 성공"));

    }

    @Tag(name = "비밀번호 변경" , description = "마이페이지에서 비밀번호 변경")
    @PutMapping("/modifypwd/{empCode}")
    public ResponseEntity<ResponseDTO> resetPassword(
                                                     @PathVariable Long empCode,
                                                     @RequestBody MyPageUpdateDTO myPageUpdateDTO,
                                                     @RequestHeader("Authorization") String authToken,
                                                     @AuthenticationPrincipal User principal) {

        // principal 여기서 토큰에서 사용자 정보를 추출하여 empCode를 얻어올 수 있음
        log.info("마이페이지 바밀번호변경 컨트롤러 시작");
        System.out.println("principal = " + principal);
        Long userEmpCode = (long) principal.getEmployeeCode();

        // 토큰에 있는 empCode와 요청의 empCode를 비교하여 다른 경우 FORBIDDEN 권한 없음 응답
        if (!userEmpCode.equals(empCode)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        log.info("마이페이지 컨트롤러 비밀번호 변경 사원코드 비교",empCode,userEmpCode);
        System.out.println("userEmpCode 나오냐 = " + userEmpCode);
        System.out.println("empCode 나오냐 = " + empCode);
//
        try {
            MyPageUpdateDTO myPageUpdateEmp = myPageService.updateEmpPwdByEmpCode(empCode, myPageUpdateDTO.getEmpPwd(), myPageUpdateDTO.getNewEmpPwd());
            System.out.println("정보나오냐" + myPageUpdateDTO.getEmpPwd());
            System.out.println("정보나오냐" + myPageUpdateDTO.getNewEmpPwd());

            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "비밀번호 변경 성공","비밀번호 변경 성공"));
        }
       catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ResponseDTO(HttpStatus.BAD_REQUEST, "비밀번호 변경 실패", "비밀번로 변경 실패"));
        }

    }


    //프로필사진 가져오기
    @GetMapping("/find/profile")
    public ResponseEntity<ResponseDTO> findMyPageProfileImg(
            @AuthenticationPrincipal User principal){
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "imageUrl", myPageService.findMyPageProfileImage(userEmployeeCode)));
    }

    //프로필 사진 업데이트
    @PutMapping("/updateprofile")
    public ResponseEntity<ResponseDTO> updateMyPageProfileImg(
            MultipartFile profileImage,Long empCode, @AuthenticationPrincipal User principal){

        if (profileImage.isEmpty()) {
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "프로필변경 실패","프로필 변경 실패"));
        }
//        Long userEmpCode = (long) principal.getEmployeeCode();
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "프로필변경 성공",myPageService.updateMyPageProfileImage(profileImage,empCode,principal)));
    }






}
