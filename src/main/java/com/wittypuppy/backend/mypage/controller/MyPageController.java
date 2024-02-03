package com.wittypuppy.backend.mypage.controller;

import com.wittypuppy.backend.common.dto.ResponseDTO;
import com.wittypuppy.backend.demo.dto.DemoDTO;
import com.wittypuppy.backend.mypage.dto.MyPageEmpDTO;
import com.wittypuppy.backend.mypage.dto.MyPageUpdateDTO;
import com.wittypuppy.backend.mypage.service.MyPageService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mypage")
@Slf4j
public class MyPageController {

    private final MyPageService myPageService;

    public MyPageController(MyPageService myPageService) {
        this.myPageService = myPageService;
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




}
