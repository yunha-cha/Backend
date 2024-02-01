package com.wittypuppy.backend.project.repository;

import com.wittypuppy.backend.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("Project_ProjectRepository")
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAllByProjectMemberList_Employee_EmployeeCode(Long employeeCode);

    List<Project> findAllByProjectMemberList_Employee_EmployeeCodeIn(List<Long> employeeCodeList);

    List<Project> findAllByProjectTitle(String projectTitle);

    List<Project> findAllByProjectTitleAndProjectMemberList_Employee_EmployeeCode(String projectTitle, Long employeeCode);

    List<Project> findAllByProjectTitleAndProjectMemberList_Employee_EmployeeCodeIn(String projectTitle, List<Long> employeeCodeList);
}
