package com.wittypuppy.backend.board.service;

import com.wittypuppy.backend.board.dto.*;
import com.wittypuppy.backend.board.entity.*;
import com.wittypuppy.backend.board.paging.Criteria;
import com.wittypuppy.backend.board.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BoardService {

    /* Post */
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostLikeRepository postLikeRepository;

    private final PostAttachmentRepository postAttachmentRepository;


    /* board */
    private final BoardRepository boardRepository;
    private final BoardMemberRepository boardMemberRepository;

    private final BoardGroupRepository boardGroupRepository;

    private final BoardEmployeeRepository boardEmployeeRepository;
    private final ModelMapper modelMapper;

    public BoardService(PostRepository postRepository, PostCommentRepository postCommentRepository, PostLikeRepository postLikeRepository, PostAttachmentRepository postAttachmentRepository, BoardRepository boardRepository, BoardMemberRepository boardMemberRepository, BoardGroupRepository boardGroupRepository, BoardEmployeeRepository boardEmployeeRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.postCommentRepository = postCommentRepository;
        this.postLikeRepository = postLikeRepository;
        this.postAttachmentRepository = postAttachmentRepository;
        this.boardRepository = boardRepository;
        this.boardMemberRepository = boardMemberRepository;
        this.boardGroupRepository = boardGroupRepository;
        this.boardEmployeeRepository = boardEmployeeRepository;
        this.modelMapper = modelMapper;
    }


    @Transactional
    public List<PostDTO> selectPostList() {
        log.info("BoardService >>> selectPostList >>> start");

        // jparepository를 통해 엔티티를 받음
        List<Post> postList = postRepository.findAllByOrderByPostDateDesc();

        // 좋아요 개수
//        List<PostLike> postLikeList = postLikeRepository.findByPostCode();

        List<PostDTO> postDTOList = postList.stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toList());

        System.out.println("postDTOList = " + postDTOList);
        log.info("BoardService >>> selectPostList >>> end");

        return postDTOList;


    }


    @Transactional
    public EmployeeDTO selectEmployee(EmployeeDTO employeeDTO) {
        Long prKey = employeeDTO.getEmployeeCode();

        Employee employee = boardEmployeeRepository.findByEmployeeCode(prKey);

        // dto -> 엔티티로 변환 / 엔티티, 변환할 dto 타입
        EmployeeDTO employeeDTOs = modelMapper.map(employee, EmployeeDTO.class);

        System.out.println("employee = " + employee);
        System.out.println("employeeDTOs = " + employeeDTOs);

        return employeeDTOs;

    }


    @Transactional
    public List<PostDTO> selectPostListByBoardCode(Long boardCode) {

        Board board = boardRepository.findById(boardCode).get();

        System.out.println("board.getBoardAccessStatus().equals(\"Y\") = " + board.getBoardAccessStatus().equals("Y"));

        if(board.getBoardAccessStatus().equals("Y")){
            List<Post> postList = postRepository.findByBoardCodeOrderByPostDateDesc(boardCode);

            // 엔티티 조회되는지 출력
            System.out.println("postList = " + postList);

            // 엔티티를 dto로 변환해서 반환하기
            List<PostDTO> postDTOList = postList.stream()
                    .map(post -> modelMapper.map(post, PostDTO.class))
                    .collect(Collectors.toList());

            return postDTOList;

        } else {

            return null;
        }

    }


    @Transactional
    public PostDTO insertPost(PostDTO postDTO,  Long employeeCode) {

        log.info("BoardService >>> insertPost >>> start");

        int result = 0;

        Employee userEmployee = boardEmployeeRepository.findByEmployeeCode(employeeCode);

        try{
            // 받은 dto를 엔티티로 변환
            Post newPost = modelMapper.map(postDTO, Post.class);

            // 등록할 때 현재 날짜로 설정
            newPost.setPostDate(LocalDateTime.now());
            newPost.setEmployee(userEmployee);
            newPost.setPostViews(0L);

            // 좋아요 개수 초기화 (아직 좋아요 개수 미구현)

            // 알림 받을 직원 설정 -> 테이블 생성?(게시판 멤버, 게시글 코드)
            return modelMapper.map(postRepository.save(newPost),PostDTO.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }


    @Transactional
    public PostDTO updatePost(PostDTO postDTO, Long postCode) {

        log.info("BoardService >>> updatePost >>> start");
        System.out.println("postDTO = " + postDTO);

        // postCode에 해당하는 엔티티 찾기
        Post entityPost = postRepository.findById(postCode).get();

        entityPost.setPostTitle(postDTO.getPostTitle());
        entityPost.setPostContext(postDTO.getPostContext());
        entityPost.setPostNoticeStatus(postDTO.getPostNoticeStatus());
        entityPost.setBoardCode(postDTO.getBoardCode());

        postRepository.save(entityPost);


        // 파일 첨부

        // 알림 설정


        //
        PostDTO updatedPostDTO = modelMapper.map(entityPost, PostDTO.class);

        System.out.println("entityPost = " + entityPost);
        System.out.println("updatedPostDTO = " + updatedPostDTO);

        return updatedPostDTO;

    }


    @Transactional
    public String deletePost(Long postCode) {

        int result = 0;
        try{

            Post deletepost = postRepository.findById(postCode).get();
            postRepository.delete(deletepost);

            result = 1;

        }catch (Exception e){
            e.printStackTrace();
        }

        return result > 0 ? "게시글 삭제 성공" : "게시글 삭제 실패";

    }

    @Transactional
    public String movePost(Long postCode, Long boardCode) {

        int result = 0;
        try {

            Post post = postRepository.findById(postCode).get();
            post.setBoardCode(boardCode);
            result = 1;

        }catch (Exception e){

            e.printStackTrace();
        }

        return result > 0 ? "게시물 이동 성공" : "게시물 이동 실패";
    }


    @Transactional
    public PostDTO selectPost(Long postCode) {


        // 조회수 카운트

        Post post = postRepository.findById(postCode).orElseThrow( () -> new RuntimeException("Post not found"));
        post.setPostViews(post.getPostViews()+1);
        postRepository.save(post);

        PostDTO postDTO = modelMapper.map(post, PostDTO.class);

        return postDTO;

    }


    // 댓글 등록
    @Transactional
    public PostCommentDTO insertComment(PostCommentDTO postCommentDTO, Long postCode, Long employeeCode) {
        Employee employee = new Employee();
        employee.setEmployeeCode(employeeCode);

        // employee 객체로 boardMember 찾는게 되나?
        BoardMember boardMember = boardMemberRepository.findByEmployee(employee);

        PostComment newPostComment = modelMapper.map(postCommentDTO, PostComment.class);
        System.out.println("newPostComment : " + newPostComment); // 값 serivce에 잘 전달되는지 확인

        newPostComment.setBoardMember(boardMember);
        newPostComment.setPostCommentDate(LocalDateTime.now());
        newPostComment.setPostCode(postCode);
        newPostComment.setPostCommentDeleteStatus("N");

        return modelMapper.map(postCommentRepository.save(newPostComment),PostCommentDTO.class);

//        PostComment newPostComment = modelMapper.map(postCommentDTO, PostComment.class);
//        newPostComment.setPostCode(postCode);
//        newPostComment.setBoardMember(boardMember);
//        newPostComment.setPostCommentDate(LocalDateTime.now());
//
//        postCommentRepository.save(newPostComment);
//
//        System.out.println("newPostComment = " + newPostComment);
//
//        return modelMapper.map(newPostComment, PostCommentDTO.class);

    }


    @Transactional
    public String deleteComment(Long commentCode) {

        int result = 0;

        try{
            PostComment postComment = postCommentRepository.findByPostCommentCode(commentCode);
            postCommentRepository.delete(postComment);
            result = 1;

        }catch (Exception e){
            e.printStackTrace();

        }
        return result > 0 ? "댓글 삭제 성공" : "댓글 삭제 실패";
    }



    @Transactional
    public String updateComment(PostCommentDTO postCommentDTO, Long commentCode) {

        int result = 0;

        try{
            PostComment postComment = postCommentRepository.findById(commentCode).get();
//            PostComment postComment = postCommentRepository.findById(commentCode).orElseThrow();
            postComment.setPostCommentContext(postCommentDTO.getPostCommentContext());
            postComment.setPostCommentUpdateDate(LocalDateTime.now());

            result = 1;

        } catch (Exception e){

            e.printStackTrace();
//            throw new RuntimeException(e);

        }

        return result > 0 ? "댓글 수정 성공" : "댓글 수정 실패";

    }


    @Transactional
    public Page<PostDTO> searchPostListWithPaging(String search, Long boardCode, Pageable pageable) {

            Page<Post> postListByTitle = postRepository.findByBoardCodeAndPostTitleLikeOrBoardCodeAndPostContextLike(boardCode, '%' + search + '%', boardCode, '%' + search + '%', pageable);

            System.out.println("postListByTitle = " + postListByTitle);

            Page<PostDTO> postDTOList = postListByTitle.map(
                    post -> modelMapper.map(post, PostDTO.class)
            );

            System.out.println("postDTOList = " + postDTOList);
            return postDTOList;


    }

    
    @Transactional
    public PostLikeDTO findByEmployeeCode(Long employeeCode) {
        PostLike postLike = postLikeRepository.findByEmployeeCode(employeeCode);
        System.out.println(postLike);
        if(postLike == null){
            return null;
        } else{
            return modelMapper.map(postLike, PostLikeDTO.class);
        }
    }


    @Transactional
    public PostLikeDTO insertPostLike(PostLikeDTO postLikeDTO) {

        PostLike postLike = postLikeRepository.save(modelMapper.map(postLikeDTO, PostLike.class));

        return modelMapper.map(postLike, PostLikeDTO.class);

    }

    @Transactional
    public String deletePostLike(PostLikeDTO postLikeDTO) {

        int result = 0;

        try{
            PostLike postLike = modelMapper.map(postLikeDTO, PostLike.class);
            postLikeRepository.delete(postLike);
            result = 1;

        }catch (Exception e){
            e.printStackTrace();

        }
        return result > 0 ? "좋아요 삭제 성공" : "좋아요 삭제 실패";

    }


    // 추후에 게시판 관리자 추가?
    @Transactional
    public String createBoard(BoardDTO boardDTO, List<BoardMemberDTO> memberDTOList) {

        // 게시판 허가 상태 default 값을 N으로
        boardDTO.setBoardAccessStatus("N");
        Board board = modelMapper.map(boardDTO, Board.class);

        boardRepository.save(board);

        List<BoardMember> boardMemberList = new ArrayList<>();
        for (BoardMemberDTO boardMemberDTO : memberDTOList) {
            boardMemberDTO.setBoardCode(board.getBoardCode());

            BoardMember boardMember = modelMapper.map(boardMemberDTO, BoardMember.class);
            boardMemberList.add(boardMember);
        }

        boardMemberRepository.saveAll(boardMemberList);

        return "게시판 저장 성공";

    }


    @Transactional
    public String deleteBoard(Long boardCode) {

        int result = 0;
        try {
            Board board = boardRepository.findById(boardCode).get();
            boardRepository.delete(board);

            // board_member도 삭제되어야 함.(안되도 문제는 없지만 사용하지 않는 데이터 남음)
            List<BoardMember> boardMemberList = boardMemberRepository.findByBoardCode(boardCode);  // 오류 : board가 삭제되서 boardCode가 null이 되서 조회가 안됨
            boardMemberRepository.deleteAll(boardMemberList);

            result = 1;

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return result == 1 ? "게시판 삭제 성공" : "게시판이 존재하지 않습니다.";

    }


    // 게시판 수정 - 미완성
    public String updateBoard(Long boardCode, BoardDTO boardDTO, List<BoardMemberDTO> memberDTOList) {

        int result = 0;

        try {

            // Board update
            Board myBoard = boardRepository.findById(boardCode).get();
            Board board = modelMapper.map(boardDTO, Board.class);

            myBoard.setBoardDescription(board.getBoardDescription());

            // BoardMember update
            List<BoardMember> memberList = boardMemberRepository.findByBoardCode(boardCode);
            List<BoardMember> members = memberDTOList.stream()
                    .map(memberDTO -> modelMapper.map(memberDTO, BoardMember.class))
                    .toList();

            for (BoardMember member : memberList) {

//                member.setEmployeeCode();
                member.setBoardMemberCreatePostStatus("N");

            }

            result = 1;

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return result > 0 ? "게시판 수정 성공" : "게시판 수정 실패";

    }


    @Transactional
    public String deletePostList(List<PostDTO> postDTOList) {

        List<Post> postList = postDTOList.stream().map(postDTO->modelMapper.map(postDTO,Post.class)).toList();

        List<Long> postCodeList = new ArrayList<>();
        for(Post post : postList){
            postCodeList.add(post.getPostCode());
        }

        postRepository.deleteAllById(postCodeList);

        return "게시글 여러 개 삭제 성공";

    }


    @Transactional
    public String noticePostList(List<PostDTO> postDTOList) {


        List<Long> ids = new ArrayList<>();
        for(PostDTO post : postDTOList){
            ids.add(post.getPostCode());
        }

        List<Post> postEntities = postRepository.findAllById(ids);
        for(Post post : postEntities){
            post.setPostNoticeStatus("Y");
        }
        postRepository.saveAll(postEntities);

        return "게시글 여러 개 공지 설정 성공";

    }

    private <S, T> List<T> convert(List<S> list, Class<T> targetClass) {
        return list.stream()
                .map(value -> modelMapper.map(value, targetClass))
                .collect(Collectors.toList());
    }



    public List<BoardMemberDTO> selectBoardMember(Long boardCode) {

        return convert(boardMemberRepository.findByBoardCode(boardCode), BoardMemberDTO.class);
    }

    public Object postAlert(Long boardCode) {

        // 현재 게시판에서 방금 등록된 게시글 조회
         postRepository.findByBoardCode(boardCode);

        // 알림 메세지를 적고 엔티티에 저장하고
        String message = "게시물이 등록되었습니다!";


        return null;

    }

    public List<PostDTO> findByEmployeeCodeMain(long employeeCode) {
        List<Post> postEntity = postRepository.findByEmployee_EmployeeCode(employeeCode);
        return convert(postEntity, PostDTO.class);
    }


    /* 페이징된 게시글 조회 */
    @Transactional
    public Page<PostDTO> selectPostListWithPaging(Pageable pageable, Long boardCode) {

        log.info("[BoardService] selectPostListWithPaging =================>");

        // cri 필드값 사용 - 현재 페이지, 페이지당 데이터 개수


            // 페이징 적용하여 데이터 조회
            Page<Post> postList = postRepository.findByBoardCode(boardCode, pageable);

            // 엔티티 조회되는지 출력
            System.out.println("postList = " + postList);

            // 엔티티를 dto로 변환
            Page<PostDTO> postDTOList = postList
                    .map(post -> modelMapper.map(post, PostDTO.class));

            return postDTOList;

    }



    /* 게시판 카테고리 조회 */
//    public List<BoardDTO> selectBoardList(Long myParentDepartmentCode) {
//
//        List<Board> boardList1 = boardRepository.findAllByBoardAccessStatusAndBoardGroupCode("Y", 1L);
//        List<Board> boardList2 = boardRepository.findAllByParentDepartmentCode(myParentDepartmentCode);
//
//        System.out.println("boardList1 = " + boardList1);
//        System.out.println("boardList2 = " + boardList2);
//
//        List<BoardDTO> boardDTOList1 = boardList1.stream()
//                .map(board -> modelMapper.map(board, BoardDTO.class))
//                .collect(Collectors.toList());
//
//        List<BoardDTO> boardDTOList2 = boardList2.stream()
//                .map(board -> modelMapper.map(board, BoardDTO.class))
//                .collect(Collectors.toList());
//
//        MyDeptBoardDTO myDeptBoardDTO = new MyDeptBoardDTO(boardDTOList1, boardDTOList2);
//
//        System.out.println("myDeptBoardDTO = " + myDeptBoardDTO);
//
//        return null;
//
//    }


    /* 게시판 카테고리 조회 */
    public MyDeptBoardDTO selectBoardList(Long myParentDepartmentCode) {

        List<Board> boardList1 = boardRepository.findAllByBoardAccessStatusAndBoardGroupCode("Y", 1L);
        List<Board> boardList2 = boardRepository.findAllByParentDepartmentCode(myParentDepartmentCode);

        System.out.println("boardList1 = " + boardList1);
        System.out.println("boardList2 = " + boardList2);

        List<BoardDTO> boardDTOList1 = boardList1.stream()
                .map(board -> modelMapper.map(board, BoardDTO.class))
                .collect(Collectors.toList());

        List<BoardDTO> boardDTOList2 = boardList2.stream()
                .map(board -> modelMapper.map(board, BoardDTO.class))
                .collect(Collectors.toList());

        MyDeptBoardDTO myDeptBoardDTO = new MyDeptBoardDTO(boardDTOList1, boardDTOList2);

        System.out.println("myDeptBoardDTO = " + myDeptBoardDTO);

        return myDeptBoardDTO;

    }

    public BoardDTO selectBoard(Long boardCode) {

        Board boardInfo = boardRepository.findById(boardCode).get();

        return modelMapper.map(boardInfo, BoardDTO.class);

    }

    public void insertPostAttachment(List<PostAttachmentDTO> attachmentDTOS) {

        postAttachmentRepository.saveAll(convert(attachmentDTOS,PostAttachment.class));
    }

    public List<PostAttachmentDTO> findByPostCode(Long postCode) {
        return convert(postAttachmentRepository.findAllByPostCode(postCode),PostAttachmentDTO.class);
    }

    public PostAttachment getFileById(Long attachmentCode) {

        return postAttachmentRepository.findById(attachmentCode).orElseThrow(null);
    }
}


