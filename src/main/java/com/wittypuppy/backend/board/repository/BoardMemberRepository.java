package com.wittypuppy.backend.board.repository;

import com.wittypuppy.backend.board.entity.BoardMember;
import com.wittypuppy.backend.board.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardMemberRepository extends JpaRepository<BoardMember,Long> {


    List<BoardMember> findByBoardCode(Long boardCode);

    BoardMember findByEmployee(Employee employee);
}
