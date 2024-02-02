package com.wittypuppy.backend.project.repository;

import com.wittypuppy.backend.project.entity.ProjectPostMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("Project_ProjectPostMemberRepository")
public interface ProjectPostMemberRepository extends JpaRepository<ProjectPostMember, Long> {
    List<ProjectPostMember> findAllByProjectPostCodeAndProjectPostMemberDeleteStatus(Long projectPostCode, String projectPostMemberDeleteStatus);

    Optional<ProjectPostMember> findByProjectPostMemberCodeAndProjectPostMemberDeleteStatus(Long projectPostMemberCode, String projectPostMemberDeleteStatus);

    Optional<ProjectPostMember> findByProjectPostCodeAndProjectMemberCodeAndProjectPostMemberDeleteStatus(Long projectPostCode, Long projectMemberCode, String projectPostMemberDeleteStatus);
}