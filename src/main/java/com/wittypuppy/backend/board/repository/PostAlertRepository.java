package com.wittypuppy.backend.board.repository;

import com.wittypuppy.backend.board.entity.Board;
import com.wittypuppy.backend.board.entity.PostAlert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostAlertRepository extends JpaRepository<PostAlert,Long> {


}
