package com.wittypuppy.backend.project.repository;

import com.wittypuppy.backend.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("Project_ProjectRepository")
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAllByProjectMemberList_Employee_EmployeeCode(Long employeeCode);

    List<Project> findAllByProjectManager_Department_DepartmentCode(Long departmentCode);

    List<Project> findAllByProjectTitleLike(String searchValuePattern);

    Optional<Project> findByProjectPostList_ProjectPostCodeAndProjectMemberList_Employee_EmployeeCode(Long projectPostCode, Long employeeCode);

    Optional<Project> findByProjectPostList_ProjectPostCode(Long projectPostCode);
}