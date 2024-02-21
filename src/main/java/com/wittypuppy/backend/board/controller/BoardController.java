package com.wittypuppy.backend.board.controller;


import com.wittypuppy.backend.Employee.dto.User;
import com.wittypuppy.backend.board.dto.*;
import com.wittypuppy.backend.board.paging.Criteria;
import com.wittypuppy.backend.board.paging.PageDTO;
import com.wittypuppy.backend.board.paging.PagingResponseDTO;
import com.wittypuppy.backend.board.service.BoardService;
import com.wittypuppy.backend.common.dto.ResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/board")
@Slf4j
@Tag(name = "게시판 스웨거 연동")
public class BoardController {

    private final BoardService boardService;

    private final SimpMessagingTemplate simp;

    public BoardController(BoardService boardService, SimpMessagingTemplate simpMessagingTemplate) {
        this.boardService = boardService;
        this.simp = simpMessagingTemplate;
    }
    @GetMapping("main-board")
    public ResponseEntity<ResponseDTO> selectMainPagePost(@AuthenticationPrincipal User user){
        List<PostDTO> postList = boardService.findByEmployeeCodeMain((long)user.getEmployeeCode());
        return res("메인페이지 post 조회 성공", postList);
    }



    /* 게시판 그룹 카테고리 조회 */
    @Tag(name = "게시판 카테고리 조회", description = "게시판 사이드바에 나오는 카테고리")
    @GetMapping("")
    public ResponseEntity<ResponseDTO> selectBoardList() {
        log.info("BoardController >>> selectBoardList >>> start");

        List<BoardDTO> boardDTOList = boardService.selectBoardList();

        return res("게시판그룹 조회", boardDTOList);
    }




    // 특정 게시판에서 게시글 정렬
    @Tag(name = "게시글 조회", description = "해당 게시판에 따른 게시글 최신순 리스트")
    @GetMapping("/{boardCode}")
    public ResponseEntity<ResponseDTO> selectPostListByBoardCode(@PathVariable Long boardCode,
                                                                 @RequestParam(defaultValue = "1") String offset) {
        log.info("BoardController >>> selectPostsOfBoard >>> start");

        // 1. 게시판 중 허가 상태가 "Y"인거 find
        List<PostDTO> postDTOList = boardService.selectPostListByBoardCode(boardCode);


        /* 페이징 */
        Criteria cri = new Criteria(Integer.valueOf(offset), 10);

        // 페이지 번호에 맞게 데이터 가져오기
        Page<PostDTO> postDTOPages = boardService.selectPostListWithPaging(cri, boardCode);

        // pageDTO 적용, 화면에서 페이징 버튼 처리
        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO(new PageDTO(cri, (Long) postDTOPages.getTotalElements()),postDTOPages);

        System.out.println("pagingResponseDTO = " + pagingResponseDTO);
        log.info("BoardController >>> selectPostsOfBoard >>> end");

        return res("페이징 적용한 조회", pagingResponseDTO);

    }


    // 게시글 등록
    @Tag(name = "게시글 등록", description = "게시글 등록")
    @PostMapping("/posts/regist")
    public ResponseEntity<ResponseDTO> insertPost(@RequestBody PostDTO postDTO, @AuthenticationPrincipal User principal){

        log.info("BoardController >> insertPost >> start");
        System.out.println("postDTO = " + postDTO);

        Long employeeCode = (long) principal.getEmployeeCode();
        String resultStr = boardService.insertPost(postDTO, employeeCode);

        return res(resultStr, null);
    }


    /* 게시글 상세 열람 */
    @Tag(name = "게시글 상세", description = "게시글 상세 보기")
    @GetMapping("/posts/{postCode}")
    public ResponseEntity<ResponseDTO> selectPost(@PathVariable Long postCode){

        System.out.println("postCode = " + postCode);

        // 게시글 열람, 댓글 조인
        PostDTO postDTO = boardService.selectPost(postCode);

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "열람 성공",postDTO));

    }


    /* 게시글 추천 */
    @Tag(name = "게시글 추천", description = "특정 게시글 좋아요 기능")
    @PostMapping ("/posts/{postCode}/like")
    public ResponseEntity<ResponseDTO> likePost(@PathVariable Long postCode){

        System.out.println("postCode = " + postCode);

        Long employeeCode = 1L;

        // 좋아요
        PostLikeDTO postLikeDTO = boardService.findByEmployeeCode(employeeCode);

        if(postLikeDTO == null){
            PostLikeDTO postLikeDTO1 = new PostLikeDTO();

            postLikeDTO1.setPostCode(postCode);
            postLikeDTO1.setEmployeeCode(employeeCode);
            postLikeDTO1 = boardService.insertPostLike(postLikeDTO1);

            return res("좋아요~~~", postLikeDTO1);

        } else{

            String result = boardService.deletePostLike(postLikeDTO);
            System.out.println("result = " + result);

            return res("좋아요 삭제", null);

        }

    }


    /* 게시글 수정 */
    @Tag(name = "게시글 수정", description = "게시글 수정")
    @PutMapping("/posts/{postCode}/update")
    public ResponseEntity<ResponseDTO> updatePost(@RequestBody PostDTO postDTO, @PathVariable Long postCode){

        log.info("BoardController >> updatePost >> start");

        // 값을 받는지 확인
        System.out.println("postDTO = " + postDTO);
        System.out.println("postCode = " + postCode);

        PostDTO updatePostDTO = boardService.updatePost(postDTO, postCode);

        return res("성공", updatePostDTO);

    }


    /* 게시글 삭제 */
    @Tag(name = "게시글 삭제", description = "게시글 삭제")
    @DeleteMapping("/posts/{postCode}")
    public ResponseEntity<ResponseDTO> deletePost(@PathVariable Long postCode){

        String resultMessage = boardService.deletePost(postCode);

        return res(resultMessage, null);
    }


    /* 게시글 이동 - 게시글은 게시판을 이동할 수 있다. */
    @Tag(name = "게시글 이동", description = "게시판 카테고리 이동")
    @PutMapping("/posts/{postCode}/move")
    public ResponseEntity<ResponseDTO> movePost(@PathVariable Long postCode, @RequestParam Long boardCode ){


       String result = boardService.movePost(postCode, boardCode);

       return res("게시글 이동", result);

    }


    /* 게시글 검색 */
    @Tag(name = "게시글 검색", description = "게시글 이름과 내용으로 검색")

    @GetMapping("/{boardCode}/posts/search")
    public ResponseEntity<ResponseDTO> searchPostList(@RequestParam(name = "q", defaultValue = "회사") String search,
                                                      @PathVariable Long boardCode,
                                                      @RequestParam(defaultValue = "1") String offset
                                                      ){


        System.out.println("search = " + search);
        System.out.println("boardCode = " + boardCode);


        /* 페이징 */
        Criteria cri = new Criteria(Integer.valueOf(offset), 10);

        // 페이지 번호에 맞게 데이터 가져오기
        Page<PostDTO> postDTOPages = boardService.searchPostListWithPaging(cri, search, boardCode);

        // pageDTO 적용, 화면에서 페이징 버튼 처리
        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO(new PageDTO(cri, (Long) postDTOPages.getTotalElements()),postDTOPages);

        System.out.println("pagingResponseDTO = " + pagingResponseDTO);
        log.info("BoardController >>> selectPostsOfBoard >>> end");


        return res("검색한 게시글 조회", pagingResponseDTO);

    }



    /* 댓글 등록 */
    @Tag(name = "댓글 등록", description = "댓글 등록")
    @PostMapping("/posts/{postCode}/comment/regist")
    public ResponseEntity<ResponseDTO> registComment(@RequestBody PostCommentDTO postCommentDTO,
                                                     @PathVariable Long postCode,
                                                     @AuthenticationPrincipal User principal
                                                     ){

        System.out.println("postCode = " + postCode);

        // 사용자 setter : commentDTO.setEmployeeCode
        System.out.println("principal = " + (long) principal.getEmployeeCode());
        Long employeeCode = (long) principal.getEmployeeCode();

        PostCommentDTO commentDTO = boardService.insertComment(postCommentDTO, postCode, employeeCode);

        return res("댓글 등록 성공", commentDTO);

    }


    /* 댓글 수정 */
    // 수정할 때 객체에 모두 넘기면 commentCode가 필요없음
    @Tag(name = "댓글 수정", description = "댓글 수정")
    @PutMapping("/posts/comment/{commentCode}")
    public ResponseEntity<ResponseDTO> updateComment(@RequestBody PostCommentDTO postCommentDTO, @PathVariable Long commentCode){

        System.out.println("postCommentDTO = " + postCommentDTO);

        String resultStr = boardService.updateComment(postCommentDTO, commentCode);


        return res("댓글 수정 성공", resultStr);
    }


    /* 댓글 삭제 */
    @Tag(name = "댓글 삭제", description = "댓글 삭제")
    @DeleteMapping("/posts/comment/{commentCode}")
    public ResponseEntity<ResponseDTO> deleteComment(@PathVariable Long commentCode){

        String resultStr = boardService.deleteComment(commentCode);

        return res("댓글 삭제 성공", resultStr);
    }



    /*********** 게시판 ***********/
    /* 게시판 생성 */
    @Tag(name = "게시판 생성", description = "게시판 생성")
    @PostMapping("/boards/create")
    public ResponseEntity<ResponseDTO> createBoard(@RequestBody BoardAndMemberDTO boardAndMemberDTO){

        String result = boardService.createBoard(boardAndMemberDTO.getBoard(), boardAndMemberDTO.getMemberList());

        return res("게시판 생성", result );

    }


    /* 게시판 수정 */
    // 카테고리랑 이름 빼고 수정할 수 있게
//    @PutMapping("/boards/{boardCode}")
//    public ResponseEntity<ResponseDTO> updateBoard(@PathVariable Long boardCode, @RequestBody BoardAndMemberDTO boardAndMemberDTO) {
//
//        BoardDTO boardDTO = boardAndMemberDTO.getBoard();
//        List<BoardMemberDTO> memberDTOList = boardAndMemberDTO.getMemberList();
//
//        String result = boardService.updateBoard(boardCode, boardDTO, memberDTOList);
//
//        return null;
//
//    }



    /* 게시판 삭제 */
    @Tag(name = "게시판 삭제", description = "게시판 삭제")
    @DeleteMapping("/boards/{boardCode}")
    public ResponseEntity<ResponseDTO> deleteBoard(@PathVariable Long boardCode) {

        String result = boardService.deleteBoard(boardCode);

        return res("게시판 삭제", result);

    }




    /********* 게시판 관리 *********/
    /* 게시판에 따른 게시글 여러 개 삭제 */
    @Tag(name = "게시판 관리", description = "게시판에서 게시글 여러 개 삭제 기능")
    @DeleteMapping("/boards/remove-posts")
    public ResponseEntity<ResponseDTO> deletePostList(@RequestBody List<PostDTO> postDTOList) {

        String result = boardService.deletePostList(postDTOList);


        return res("게시글 여러 개 삭제", result);
    }



    /* 여러 개의 게시글을 공지글로 설정 */
    @Tag(name = "게시판 관리", description = "게시글을 공지글로 설정")
    @PutMapping("/boards/notice-posts")
    public ResponseEntity<ResponseDTO> noticePostList(@RequestBody List<PostDTO> postDTOList) {

        String result = boardService.noticePostList(postDTOList);

        return res("게시글 여러 개 공지글로 설정", result);

    }



    /* 게시글 등록 시 알림 전송 */
    @Tag(name = "게시글 알림", description = "특정 직원에게 게시글을 등록 시 알림을 전송")
    @MessageMapping("/boards/{boardCode}/alert")
    public void postAlert(@Payload PostDTO postDTO, @PathVariable Long boardCode){

        // 보드 멤버 조회해서 사용자에게 전달
        List<BoardMemberDTO> memberDTOList = boardService.selectBoardMember(boardCode);

        System.out.println("memberDTOList = " + memberDTOList);


        simp.convertAndSend("/topic/boards/alert/"+ 2,    //구독한 사람들한테 메세지, 사용자2 : 알림 보내기
                boardService.postAlert(boardCode)); // 보낼 데이터, dto alert를 보냄
    }



    /* 게시판에 따른 게시글 순서 이동 */
    @GetMapping("/test")
    public ResponseEntity<ResponseDTO> selectEmployee(@RequestBody EmployeeDTO employeeDTO){

        log.info("test Controller");

        EmployeeDTO employee = boardService.selectEmployee(employeeDTO);

        System.out.println("controller employee = " + employee);

        return res("성공", employee);

    }



    /**
     * 에러를 갖고 응답하는 메소드
     * @param errorCode 에러코드
     * @param message 메세지
     * @return 에러코드,메세지,null 로 응답
     */
    private ResponseEntity<ResponseDTO> resNull(int errorCode, String message){
        return ResponseEntity.ok().body(new ResponseDTO(errorCode, message,null));
    }

    /**
     * 정상적으로 응답하는 메소드
     * @param msg 메세지
     * @param data 보낼 데이터
     * @return 200, 메세지, 보낼데이터 로 응답
     */
    private ResponseEntity<ResponseDTO> res(String msg,Object data){
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,msg,data));
    }


}
