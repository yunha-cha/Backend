package com.wittypuppy.backend.project.service;

import com.wittypuppy.backend.project.entity.ProjectPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectPostRepository extends JpaRepository<ProjectPost,Long> {
}
