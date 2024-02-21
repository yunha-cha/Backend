package com.wittypuppy.backend.board.repository;

import com.wittypuppy.backend.board.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findAllByOrderByPostDateDesc();

    List<Post> findByBoardCodeOrderByPostDateDesc(Long boardCode);

    Page<Post> findByBoardCodeAndPostTitleLikeOrBoardCodeAndPostContextLike(Long boardCode, String s, Long boardCode1, String s1, Pageable paging);


    void findByBoardCode(Long boardCode);

    Page<Post> findByBoardCode(Long boardCode, Pageable paging);

    List<Post> findByEmployee_EmployeeCode(long employeeCode);
}
