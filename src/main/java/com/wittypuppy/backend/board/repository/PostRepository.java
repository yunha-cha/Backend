package com.wittypuppy.backend.board.repository;

import com.wittypuppy.backend.board.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findByBoardCode(Long boardCode);

    Post findByPostCode(Long postCode);
}
