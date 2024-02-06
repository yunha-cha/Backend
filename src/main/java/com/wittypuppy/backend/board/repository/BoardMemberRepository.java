package com.wittypuppy.backend.board.repository;

import com.wittypuppy.backend.board.entity.Board;
import com.wittypuppy.backend.board.entity.BoardMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardMemberRepository extends JpaRepository<BoardMember,Long> {


    List<BoardMember> findByBoardCode(Long boardCode);



}
