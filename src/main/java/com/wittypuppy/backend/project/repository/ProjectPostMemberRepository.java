package com.wittypuppy.backend.project.repository;

import com.wittypuppy.backend.project.entity.ProjectPostMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectPostMemberRepository extends JpaRepository<ProjectPostMember,Long> {
    Optional<ProjectPostMember> findByProjectPostCodeAndProjectMemberCode(Long projectPostCode, Long projectMemberCode);
}
