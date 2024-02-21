package com.wittypuppy.backend.board.repository;

import com.wittypuppy.backend.board.entity.BoardGroup;
import com.wittypuppy.backend.board.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardGroupRepository extends JpaRepository<BoardGroup,Long> {


}
