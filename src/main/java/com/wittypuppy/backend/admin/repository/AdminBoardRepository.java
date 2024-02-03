package com.wittypuppy.backend.admin.repository;

import com.wittypuppy.backend.admin.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminBoardRepository extends JpaRepository<Board,Long> {
    List<Object> findAllByBoardAccessStatus(String n);
}
