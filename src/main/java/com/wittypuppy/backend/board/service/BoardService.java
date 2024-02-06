package com.wittypuppy.backend.board.service;

import com.wittypuppy.backend.board.dto.*;
import com.wittypuppy.backend.board.entity.*;
import com.wittypuppy.backend.board.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BoardService {

    /* Post */
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostLikeRepository postLikeRepository;


    /* board */
    private final BoardRepository boardRepository;
    private final BoardMemberRepository boardMemberRepository;


    private final BoardEmployeeRepository boardEmployeeRepository;
    private final ModelMapper modelMapper;

    public BoardService(PostRepository postRepository, PostCommentRepository postCommentRepository, PostLikeRepository postLikeRepository, BoardRepository boardRepository, BoardMemberRepository boardMemberRepository, BoardEmployeeRepository boardEmployeeRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.postCommentRepository = postCommentRepository;
        this.postLikeRepository = postLikeRepository;
        this.boardRepository = boardRepository;
        this.boardMemberRepository = boardMemberRepository;
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
    public String insertPost(PostDTO postDTO) {

        log.info("BoardService >>> insertPost >>> start");

        int result = 0;

        try{
            // 받은 dto를 엔티티로 변환
            Post newPost = modelMapper.map(postDTO, Post.class);

            // 등록할 때 현재 날짜로 설정
            newPost.setPostDate(LocalDateTime.now());

            // 알림 받을 직원 설정 -> 테이블 생성?(게시판 멤버, 게시글 코드)
            postRepository.save(newPost);
            result = 1;

        } catch (Exception e) {
            e.printStackTrace();
        }


        return result > 0 ? "게시물 등록 성공" : "게시물 등록 실패";

    }


    @Transactional
    public PostDTO updatePost(PostDTO postDTO, Long postCode) {

        log.info("BoardService >>> updatePost >>> start");
        System.out.println("postDTO = " + postDTO);

        // postCode에 해당하는 엔티티 찾기
        Post entityPost = postRepository.findById(postCode).get();
        entityPost.setPostTitle(postDTO.getPostTitle());
        entityPost.setPostContext(postDTO.getPostContext());

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


    @Transactional
    public PostCommentDTO insertComment(PostCommentDTO postCommentDTO, Long postCode) {

        PostComment newPostComment = modelMapper.map(postCommentDTO, PostComment.class);
        newPostComment.setPostCode(postCode);
        newPostComment.setPostCommentDate(LocalDateTime.now());

        postCommentRepository.save(newPostComment);

        System.out.println("newPostComment = " + newPostComment);

        return modelMapper.map(newPostComment, PostCommentDTO.class);

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
    public List<PostDTO> searchPostList(String search, Long boardCode) {

//        List<Post> postList = postRepository.findByBoardCodeOrderByPostDateDesc(boardCode);

        List<Post> postListByTitle = postRepository.findByBoardCodeAndPostTitleLikeOrBoardCodeAndPostContextLike(boardCode, '%' + search + '%', boardCode, '%' + search + '%');

        List<PostDTO> postDTOList = postListByTitle.stream().map(
                post -> modelMapper.map(post, PostDTO.class)
        ).toList();


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
            System.out.println("dpd?");
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
}


