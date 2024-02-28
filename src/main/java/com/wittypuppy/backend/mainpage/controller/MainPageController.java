package com.wittypuppy.backend.mainpage.controller;

import com.wittypuppy.backend.common.dto.ResponseDTO;
import com.wittypuppy.backend.mainpage.dto.MainPageBoardDTO;
import com.wittypuppy.backend.mainpage.dto.MainPageProjectListDTO;
import com.wittypuppy.backend.mainpage.service.MainPageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
