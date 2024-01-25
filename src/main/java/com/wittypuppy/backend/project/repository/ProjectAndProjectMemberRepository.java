package com.wittypuppy.backend.project.repository;

import com.wittypuppy.backend.project.entity.ProjectAndProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectAndProjectMemberRepository extends JpaRepository<ProjectAndProjectMember,Long> {
}
