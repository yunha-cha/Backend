package com.wittypuppy.backend.project.repository;

import com.wittypuppy.backend.project.entity.ProjectPostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("Project_ProjectPostCommentRepository")
public interface ProjectPostCommentRepository extends JpaRepository<ProjectPostComment, Long> {
    List<ProjectPostComment> findAllByProjectPostCode(Long projectPostCode);
}