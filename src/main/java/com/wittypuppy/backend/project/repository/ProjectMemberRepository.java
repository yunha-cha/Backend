package com.wittypuppy.backend.project.repository;

import com.wittypuppy.backend.project.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
}
