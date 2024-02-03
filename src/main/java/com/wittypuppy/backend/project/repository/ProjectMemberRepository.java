package com.wittypuppy.backend.project.repository;

import com.wittypuppy.backend.project.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("Project_ProjectMemberRepository")
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    Optional<ProjectMember> findByProjectCodeAndEmployee_EmployeeCode(Long projectCode, Long EmployeeCode);
    Optional<ProjectMember> findByProjectCodeAndEmployee_EmployeeCodeAndProjectMemberDeleteStatus(Long projectCode, Long EmployeeCode, String projectMemberDeleteStatus);
}
