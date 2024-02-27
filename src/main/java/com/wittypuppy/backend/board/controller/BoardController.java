package com.wittypuppy.backend.board.controller;


import com.wittypuppy.backend.Employee.dto.User;
import com.wittypuppy.backend.board.dto.*;
import com.wittypuppy.backend.board.entity.PostAttachment;
import com.wittypuppy.backend.board.paging.Criteria;
import com.wittypuppy.backend.board.paging.PageDTO;
import com.wittypuppy.backend.board.paging.PagingResponseDTO;
import com.wittypuppy.backend.board.service.BoardService;
import com.wittypuppy.backend.common.dto.ResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.wittypuppy.backend.util.FileUploadUtils.saveFile;


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
    public ResponseEntity<ResponseDTO> selectBoardList(@AuthenticationPrincipal User principal) {
        log.info("BoardController >>> selectBoardList >>> start");

        Long myParentDepartmentCode = (long) principal.getDepartment().getParentDepartmentCode();
        System.out.println("principal.getDepartment() = " + principal.getDepartment().getParentDepartmentCode());

        MyDeptBoardDTO myDeptBoardDTO = boardService.selectBoardList(myParentDepartmentCode);

        return res("게시판그룹 조회", myDeptBoardDTO);

    }



    /* 특정 게시판 상세 조회 */
    @Tag(name = "게시판 카테고리 조회", description = "게시판 사이드바에 나오는 카테고리")
    @GetMapping("/{boardCode}")
    public ResponseEntity<ResponseDTO> selectBoard(@PathVariable Long boardCode) {
        log.info("BoardController >>> selectBoard >>> start");

        BoardDTO boardDTO = boardService.selectBoard(boardCode);

        System.out.println("boardDTO 상세 = " + boardDTO);

        return res("게시판 상세 조회", boardDTO);

    }



    // 특정 게시판에서 게시글 조회
    @Tag(name = "게시글 조회", description = "해당 게시판에 따른 게시글 최신순 리스트")
    @GetMapping("/{boardCode}/posts")
    public ResponseEntity<ResponseDTO> selectPostListByBoardCode(@PathVariable Long boardCode,
                                                                 @RequestParam Integer offset) {
        log.info("BoardController >>> selectPostsOfBoard >>> start  "+offset);

        Pageable pageable = PageRequest.of(offset, 10, Sort.by("postDate").descending());


        /* 페이징 */
        Criteria cri = new Criteria(Integer.valueOf(offset), 10);

        // 페이지 번호에 맞게 데이터 가져오기
        Page<PostDTO> postDTOPages = boardService.selectPostListWithPaging(pageable, boardCode);



        log.info("BoardController >>> selectPostsOfBoard >>> end");

        return res("페이징 적용한 조회", postDTOPages);

    }



//    // 게시글 등록
//    @Tag(name = "게시글 등록", description = "게시글 등록")
//    @PostMapping("/posts/regist")
//    public ResponseEntity<ResponseDTO> insertPost(@RequestBody PostDTO postDTO, @AuthenticationPrincipal User principal){
//
//        log.info("BoardController >> insertPost >> start");
//        System.out.println("postDTO = " + postDTO);
//
//        Long employeeCode = (long) principal.getEmployeeCode();
//        System.out.println("employeeCode = " + employeeCode);
//        String resultStr = null;
//        try{
//            boardService.insertPost(postDTO, employeeCode);
//            resultStr = "성공";
//        } catch (Exception e){
//            resultStr = "실패";
//        }
//        return res(resultStr, null);
//    }



    @Tag(name = "게시글, 파일 업로드", description = "파일 등록")
    @PostMapping("/posts/regist")
    public ResponseEntity<ResponseDTO> uploadFiles(@ModelAttribute PostDTO postDTO, List<MultipartFile> multipartFile, @AuthenticationPrincipal User user){

        if(multipartFile != null){
            PostDTO result = boardService.insertPost(postDTO, (long) user.getEmployeeCode());   //이메일을 기본키로 찾아옴
            List<PostAttachmentDTO> attachmentDTOS = new ArrayList<>();    //첨부파일 배열만듦
            for(int i =0; i<multipartFile.size(); i++){     //첨부파일 배열만큼 반복
                PostAttachmentDTO postAttachmentDTO = new PostAttachmentDTO();   //첨부파일 객체 만들음
                postAttachmentDTO.setPostAttachmentDate(new Date());       //첨부파일 들어간 날짜,시간을 지금으로
                postAttachmentDTO.setPostDeleteStatus("N");      //첨부파일 삭제 여부 N으로
                postAttachmentDTO.setPostCode(result.getPostCode());                //첨부파일 이메일 코드를 가져온 값으로 설정
                postAttachmentDTO.setPostAttachmentOgFile(multipartFile.get(i).getOriginalFilename()); //multipartFile 객체로 가져온 파일의 i번 째의 원본 파일 이름을 첨부파일 객체의 원본파일 이름으로 설정
                String fileName = UUID.randomUUID().toString().replace("-", "");    //랜덤한 이름 만들기
                try {
                    //saveFile 메서드 : util패키지에 static으로 존재함
                    postAttachmentDTO.setPostAttachmentChangedFile(saveFile("src/main/resources/static/web-files", //인자 1 : 파일 저장 위치
                            fileName,   //인자 2 : 아까 랜덤하게 만든 새로운 파일 이름
                            multipartFile.get(i)));     //MultipartFile의 i 번째 (가져온 첨부파일)
                }catch (IOException e){     //저장하다가 에러나면?
                    System.err.println(e.getMessage()); //메세지 출력
                }
                attachmentDTOS.add(postAttachmentDTO);     //아까 만든 ArrayList에 지금까지 한거 넣기
            }   //반복분 종료 (MultipartFile 개수 만큼{가져온 첨부파일 개수 만큼} 반복함)
            boardService.insertPostAttachment(attachmentDTOS);  //반복문 끝나고 만들어진 ArrayList를 서비스로 가져가기 SaveAll(어레이리스트) 하면 됨.

            return res("게시글 등록",null);
        }
        else {
            System.out.println("멀티파트 파일 없을 때도 추가");
            PostDTO result = boardService.insertPost(postDTO, (long) user.getEmployeeCode());   //이메일을 기본키로 찾아
            return res("게시글 첨부파일 안해도 등록",result);
        }

    }




    /* 게시글 수정 */
    @Tag(name = "게시글 수정", description = "게시글 수정")
    @PutMapping("/posts/{postCode}/update")
    public ResponseEntity<ResponseDTO> updatePost(@ModelAttribute PostDTO postDTO, List<MultipartFile> multipartFile, @PathVariable Long postCode){

        log.info("BoardController >> updatePost >> start");
        // 값을 받는지 확인
        System.out.println("postDTO = " + postDTO);
        System.out.println("multipartFile = " + multipartFile);


        if(multipartFile != null){
            PostDTO updatePostDTO = boardService.updatePost(postDTO, postCode);

            List<PostAttachmentDTO> attachmentDTOS = new ArrayList<>();    //첨부파일 배열만듦
            for(int i =0; i<multipartFile.size(); i++){     //첨부파일 배열만큼 반복
                PostAttachmentDTO postAttachmentDTO = new PostAttachmentDTO();   //첨부파일 객체 만들음
                postAttachmentDTO.setPostAttachmentDate(new Date());       //첨부파일 들어간 날짜,시간을 지금으로
                postAttachmentDTO.setPostDeleteStatus("N");      //첨부파일 삭제 여부 N으로
                postAttachmentDTO.setPostCode(updatePostDTO.getPostCode());                //첨부파일 이메일 코드를 가져온 값으로 설정
                postAttachmentDTO.setPostAttachmentOgFile(multipartFile.get(i).getOriginalFilename()); //multipartFile 객체로 가져온 파일의 i번 째의 원본 파일 이름을 첨부파일 객체의 원본파일 이름으로 설정
                String fileName = UUID.randomUUID().toString().replace("-", "");    //랜덤한 이름 만들기
                try {
                    //saveFile 메서드 : util패키지에 static으로 존재함
                    postAttachmentDTO.setPostAttachmentChangedFile(saveFile("src/main/resources/static/web-files", //인자 1 : 파일 저장 위치
                            fileName,   //인자 2 : 아까 랜덤하게 만든 새로운 파일 이름
                            multipartFile.get(i)));     //MultipartFile의 i 번째 (가져온 첨부파일)
                }catch (IOException e){     //저장하다가 에러나면?
                    System.err.println(e.getMessage()); //메세지 출력
                }
                attachmentDTOS.add(postAttachmentDTO);     //아까 만든 ArrayList에 지금까지 한거 넣기
            }   //반복분 종료 (MultipartFile 개수 만큼{가져온 첨부파일 개수 만큼} 반복함)
            boardService.insertPostAttachment(attachmentDTOS);  //반복문 끝나고 만들어진 ArrayList를 서비스로 가져가기 SaveAll(어레이리스트) 하면 됨.

            return res("게시글 수정 성공", updatePostDTO);
        }
        else {
            System.out.println("멀티파트 파일 없을 때 추가");
            PostDTO updatePostDTO = boardService.updatePost(postDTO, postCode);
            return res("게시글 첨부파일 안해도 수정",updatePostDTO);
        }


    }




    /* 파일 다운로드 */
    @Tag(name = "다운로드", description = "이메일의 첨부파일 다운로드")
    @GetMapping("/file-download/{attachmentCode}")
    public ResponseEntity<Resource> findByAttachmentCode(@PathVariable Long attachmentCode){
        System.out.println("??");
        try {
            PostAttachment fileEntity = boardService.getFileById(attachmentCode);  //
            Path filePath = Paths.get("src/main/resources/static/web-files"+"/"+ fileEntity.getPostAttachmentChangedFile());
            File file = filePath.toFile();
            System.out.println(file);
            if (!file.exists()) {
                throw new FileNotFoundException("파일을 찾을 수 없습니다: " + filePath);
            }
            System.out.println("aa");
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, fileEntity.getPostAttachmentChangedFile());
            headers.add(HttpHeaders.CONTENT_TYPE, Files.probeContentType(filePath)); // 파일 타입을 자동으로 결정
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length())); // 파일 크기 설정
            headers.add(HttpHeaders.EXPIRES, fileEntity.getPostAttachmentOgFile());
            System.out.println("=================="+fileEntity.getPostAttachmentOgFile());

            Resource resource = new InputStreamResource(new FileInputStream(file));

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);

        } catch (IOException e) {
            throw new RuntimeException("파일 읽기 중 오류 발생", e);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }

    }

    /* 게시글 상세 열람 */
    @Tag(name = "게시글 상세", description = "게시글 상세 보기")
    @GetMapping("/posts/{postCode}")
    public ResponseEntity<ResponseDTO> selectPost(@PathVariable Long postCode){

        System.out.println("postCode = " + postCode);

        // 게시글 열람, 댓글 조인
        PostDTO postDTO = boardService.selectPost(postCode);
        List<PostAttachmentDTO> postAttachmentDTO = boardService.findByPostCode(postDTO.getPostCode());
        postDTO.setPostAttachmentList(postAttachmentDTO);
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
    public ResponseEntity<ResponseDTO> searchPostList(@RequestParam(name = "q", defaultValue = "") String search,
                                                      @PathVariable Long boardCode,
                                                      @RequestParam(defaultValue = "0") Integer offset
                                                      ){
        if(search == null || search.isEmpty()){

            return res("검색어가 없습니다.", null);

        } else{

            Pageable pageable = PageRequest.of(offset, 10, Sort.by("postDate").descending());
            // 페이지 번호에 맞게 데이터 가져오기
            Page<PostDTO> postDTOPages = boardService.searchPostListWithPaging(search, boardCode, pageable);



            log.info("BoardController >>> selectPostsOfBoard >>> end");


            return res("검색한 게시글 조회", postDTOPages);

        }




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
