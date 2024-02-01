package com.wittypuppy.backend.board.repository;

import com.wittypuppy.backend.board.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment,Long> {


    PostComment findByPostCommentCode(Long commentCode);
}
