package com.wittypuppy.backend.project.repository;

import com.wittypuppy.backend.project.entity.ProjectPostCommentFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("Project_ProjectPostCommentFileRepository")
public interface ProjectPostCommentFileRepository extends JpaRepository<ProjectPostCommentFile, Long> {
}
