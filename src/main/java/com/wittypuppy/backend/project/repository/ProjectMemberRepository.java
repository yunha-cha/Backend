package com.wittypuppy.backend.project.repository;

import com.wittypuppy.backend.project.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("Project_ProjectMemberRepository")
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    List<ProjectMember> findAllByProjectCodeAndProjectMemberDeleteStatus(Long projectCode, String projectMemberDeleteStatus);

    Optional<ProjectMember> findByProjectCodeAndProjectMemberDeleteStatusAndEmployee_EmployeeCode(Long projectCode, String projectMemberDeleteStatus, Long employeeCode);

}