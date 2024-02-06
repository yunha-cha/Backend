package com.wittypuppy.backend.mainpage.controller;

import com.wittypuppy.backend.common.dto.ResponseDTO;
import com.wittypuppy.backend.mainpage.dto.MainPageBoardDTO;
import com.wittypuppy.backend.mainpage.service.MainPageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mainpage")
public class MainPageController {

    private final MainPageService mainPageService;

    public MainPageController(MainPageService mainPageService) {
        this.mainPageService = mainPageService;
    }


//    // 게시판에서 게시글 최신순대로 정렬
//    @GetMapping("/")
//    public ResponseEntity<ResponseDTO> selectPostList() {
//        log.info("BoardController >>> selectPostList >>> start");
//
//        List<PostDTO> postDTOList = boardService.selectPostList();
//
//        log.info("BoardController >>> selectPostList >>> end");
//
//        System.out.println("postDTOList = " + postDTOList);
//
//        return res("성공", postDTOList);
//
//    }

//    /**
//     * 정상적으로 응답하는 메소드
//     * @param msg 메세지
//     * @param data 보낼 데이터
//     * @return 200, 메세지, 보낼데이터 로 응답
//     */
//    private ResponseEntity<ResponseDTO> res(String msg,Object data){
//        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,msg,data));
//    }


    @GetMapping("/postlist")
    public ResponseEntity<ResponseDTO> selectPostList(){
        System.out.println("메인페이지 게시판 출력 컨트롤러 시작");
        List<MainPageBoardDTO> mainPageBoardDTOList = mainPageService.selectPostList();
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"게시판 조회 성공", mainPageBoardDTOList));
//        return res("성공", mainPageBoardDTOList);
    }

}
