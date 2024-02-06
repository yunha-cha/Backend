package com.wittypuppy.backend.mainpage.repository;

import com.wittypuppy.backend.board.entity.Post;
import com.wittypuppy.backend.mainpage.entity.MainPagePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("MainPage_Post_Repository")
public interface MainPagePostRepository extends JpaRepository<MainPagePost,Long> {

//    게시글찾기
    List<MainPagePost> findAllByOrderByPostDateDesc();
//    여기서 이제 4개 정도만 찾아오면됨


}
