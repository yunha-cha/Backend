package com.wittypuppy.backend.board.controller;


import com.wittypuppy.backend.board.dto.EmployeeDTO;
import com.wittypuppy.backend.board.dto.PostCommentDTO;
import com.wittypuppy.backend.board.dto.PostDTO;
import com.wittypuppy.backend.board.service.BoardService;
import com.wittypuppy.backend.common.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board")
@Slf4j
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }


    // 게시판에서 게시글 최신순대로 정렬
    @GetMapping("/")
    public ResponseEntity<ResponseDTO> selectPostList() {
        log.info("BoardController >>> selectPostList >>> start");

        List<PostDTO> postDTOList = boardService.selectPostList();

        log.info("BoardController >>> selectPostList >>> end");

        System.out.println("postDTOList = " + postDTOList);

        return res("성공", postDTOList);

    }


    // 특정 게시판에서 게시글 정렬
    @GetMapping("/{boardCode}")
    public ResponseEntity<ResponseDTO> selectPostListByBoardCode(@PathVariable Long boardCode) {
        log.info("BoardController >>> selectPostsOfBoard >>> start");

        List<PostDTO> postDTOList = boardService.selectPostListByBoardCode(boardCode);

        log.info("BoardController >>> selectPostsOfBoard >>> end");

        System.out.println("boardCode = " + boardCode);

        return res("성공", postDTOList);

    }


    // 게시글 등록
    @PostMapping("/posts/regist")
    public ResponseEntity<ResponseDTO> insertPost(@RequestBody PostDTO postDTO){

        log.info("BoardController >> insertPost >> start");
        System.out.println("postDTO = " + postDTO);

        //
        String resultStr = boardService.insertPost(postDTO);

        return res(resultStr, null);
    }


    /* 게시글 상세 열람 */
    @GetMapping("/posts/{postCode}")
    public ResponseEntity<ResponseDTO> selectPost(@PathVariable Long postCode){

        System.out.println("postCode = " + postCode);

        // 게시글 열람, 댓글 조인
        PostDTO postDTO = boardService.selectPost(postCode);

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "열람 성공",postDTO));

    }


    /* 게시글 수정 */
    @PutMapping("/posts/{postCode}")
    public ResponseEntity<ResponseDTO> updatePost(@RequestBody PostDTO postDTO, @PathVariable Long postCode){

        log.info("BoardController >> updatePost >> start");

        // 값을 받는지 확인
        System.out.println("postDTO = " + postDTO);
        System.out.println("postCode = " + postCode);

        PostDTO updatePostDTO = boardService.updatePost(postDTO, postCode);


        return res("성공", updatePostDTO);

    }


    /* 게시글 삭제 */
    @DeleteMapping("/posts/{postCode}")
    public ResponseEntity<ResponseDTO> deletePost(@PathVariable Long postCode){

        String resultMessage = boardService.deletePost(postCode);

        return res(resultMessage, null);
    }


    /* 게시글 이동 - 게시글은 게시판을 이동할 수 있다. */
    @PutMapping("/posts/{postCode}/move")
    public ResponseEntity<ResponseDTO> movePost(@PathVariable Long postCode, @RequestParam Long boardCode ){


       String result = boardService.movePost(postCode, boardCode);

       return res("게시글 이동", result);

    }


    /* 게시글 검색 */
    @GetMapping("/{boardCode}/posts/search")
    public ResponseEntity<ResponseDTO> searchPostList(@RequestParam(name = "q", defaultValue = "회사") String search, @PathVariable Long boardCode){


        System.out.println("search = " + search);
        System.out.println("boardCode = " + boardCode);
        List<PostDTO> postDTOList = boardService.searchPostList(search, boardCode);

        return res("검색 성공", postDTOList);

    }


    /* 게시글 */


    /* 게시글 추천 */
    @PutMapping("")





    /* 댓글 등록 */
    @PostMapping("/posts/{postCode}/comment/regist")
    public ResponseEntity<ResponseDTO> registComment(@RequestBody PostCommentDTO postCommentDTO ,@PathVariable Long postCode){

        System.out.println("postCode = " + postCode);

        PostCommentDTO commentDTO = boardService.insertComment(postCommentDTO, postCode);

        return res("댓글 등록 성공", commentDTO);

    }


    /* 댓글 수정 */
    // 수정할 때 객체에 모두 넘기면 commentCode가 필요없음
    @PutMapping("/posts/comment/{commentCode}")
    public ResponseEntity<ResponseDTO> updateComment(@RequestBody PostCommentDTO postCommentDTO, @PathVariable Long commentCode){

        System.out.println("postCommentDTO = " + postCommentDTO);

        String resultStr = boardService.updateComment(postCommentDTO, commentCode);


        return res("댓글 수정 성공", resultStr);
    }


    /* 댓글 삭제 */
    @DeleteMapping("/posts/comment/{commentCode}")
    public ResponseEntity<ResponseDTO> deleteComment(@PathVariable Long commentCode){

        String resultStr = boardService.deleteComment(commentCode);

        return res("댓글 삭제 성공", resultStr);
    }




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
