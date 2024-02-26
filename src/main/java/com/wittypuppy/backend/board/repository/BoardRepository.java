package com.wittypuppy.backend.board.repository;

import com.wittypuppy.backend.board.entity.Board;
import com.wittypuppy.backend.board.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long> {


    List<Board> findAllByBoardAccessStatusAndBoardGroupCode(String status, Long boardGroupCode);

    @Query("SELECT b FROM BOARD_BOARD b " +
            "LEFT JOIN BOARD_BOARD_GROUP bg ON b.boardGroupCode = bg.boardGroupCode " +
            "LEFT JOIN BOARD_BOARD_DEPT_INFO r ON b.boardCode = r.boardCode " +
            "LEFT JOIN BOARD_DEPARTMENT d ON r.departmentCode = d.departmentCode " +
            "WHERE r.departmentCode = :myParentDepartmentCode OR d.parentDepartmentCode = :myParentDepartmentCode")
    List<Board> findAllByParentDepartmentCode(@Param("myParentDepartmentCode") Long myParentDepartmentCode);


}
