package com.wittypuppy.backend.board.repository;

import com.wittypuppy.backend.board.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findAllByOrderByPostDateDesc();

    List<Post> findByBoardCodeOrderByPostDateDesc(Long boardCode);

    List<Post> findByBoardCodeAndPostTitleLikeOrBoardCodeAndPostContextLike(Long boardCode, String s, Long boardCode1, String s1);

}
