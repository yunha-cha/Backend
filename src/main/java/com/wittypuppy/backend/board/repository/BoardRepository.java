package com.wittypuppy.backend.board.repository;

import com.wittypuppy.backend.board.entity.Board;
import com.wittypuppy.backend.board.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long> {


}
