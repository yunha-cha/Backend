package com.wittypuppy.backend.board.repository;

import com.wittypuppy.backend.board.dto.PostLikeDTO;
import com.wittypuppy.backend.board.entity.Post;
import com.wittypuppy.backend.board.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostLike,Long> {

    PostLike findByEmployeeCode(Long employeeCode);



}
