package com.wittypuppy.backend.mainpage.service;

import com.wittypuppy.backend.mainpage.dto.MainPageBoardDTO;
import com.wittypuppy.backend.mainpage.entity.MainPagePost;
import com.wittypuppy.backend.mainpage.repository.MainPagePostRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MainPageService {

    private final MainPagePostRepository mainPagePostRepository;

    private final ModelMapper modelMapper;

    public MainPageService(MainPagePostRepository mainPagePostRepository, ModelMapper modelMapper) {
        this.mainPagePostRepository = mainPagePostRepository;
        this.modelMapper = modelMapper;
    }

//    @Transactional
//    public List<PostDTO> selectPostList() {
//        log.info("BoardService >>> selectPostList >>> start");
//
//        // jparepository를 통해 엔티티를 받음
//        List<Post> postList = postRepository.findAllByOrderByPostDateDesc();
//
//        List<PostDTO> postDTOList = postList.stream()
//                .map(post -> modelMapper.map(post, PostDTO.class))
//                .collect(Collectors.toList());
//
//        System.out.println("postDTOList = " + postDTOList);
//        log.info("BoardService >>> selectPostList >>> end");
//
//        return postDTOList;
//
//
//    }

    @Transactional
    public List<MainPageBoardDTO> selectPostList(){
        log.info("메인페이지 게시판 리스트출력 서비스 시작");
        List<MainPagePost> postList = mainPagePostRepository.findAllByOrderByPostDateDesc();
        List<MainPageBoardDTO> mainPageBoardDTOList = postList.stream()
                .map(post -> modelMapper.map(post, MainPageBoardDTO.class))
                .collect(Collectors.toList());

        System.out.println("mainPageBoardDTOList = " + mainPageBoardDTOList);
        log.info("서비스 끝");
        return mainPageBoardDTOList;

    }
}
