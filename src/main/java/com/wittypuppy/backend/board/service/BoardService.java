package com.wittypuppy.backend.board.service;

import com.wittypuppy.backend.board.dto.EmployeeDTO;
import com.wittypuppy.backend.board.dto.PostCommentDTO;
import com.wittypuppy.backend.board.dto.PostDTO;
import com.wittypuppy.backend.board.entity.Employee;
import com.wittypuppy.backend.board.entity.Post;
import com.wittypuppy.backend.board.entity.PostComment;
import com.wittypuppy.backend.board.repository.BoardEmployeeRepository;
import com.wittypuppy.backend.board.repository.PostCommentRepository;
import com.wittypuppy.backend.board.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BoardService {
    private final PostRepository postRepository;

    private final PostCommentRepository postCommentRepository;
    private final BoardEmployeeRepository boardEmployeeRepository;
    private final ModelMapper modelMapper;

    public BoardService(PostRepository postRepository, PostCommentRepository postCommentRepository, BoardEmployeeRepository boardEmployeeRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.postCommentRepository = postCommentRepository;
        this.boardEmployeeRepository = boardEmployeeRepository;
        this.modelMapper = modelMapper;
    }


    @Transactional
    public List<PostDTO> selectPostList() {
        log.info("BoardService >>> selectPostList >>> start");

        // jparepository를 통해 엔티티를 받음
        List<Post> postList = postRepository.findAllByOrderByPostDateDesc();

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

        List<Post> postList = postRepository.findByBoardCodeOrderByPostDateDesc(boardCode);

        // 엔티티 조회되는지 출력
        System.out.println("postList = " + postList);

        // 엔티티를 dto로 변환해서 반환하기
        List<PostDTO> postDTOList = postList.stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toList());

        return postDTOList;

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



    private <S, T> List<T> convert(List<S> list, Class<T> targetClass) {
        return list.stream()
                .map(value -> modelMapper.map(value, targetClass))
                .collect(Collectors.toList());
    }
}


