package com.wittypuppy.backend.project.repository;

import com.wittypuppy.backend.project.entity.ProjectAndProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectAndProjectMemberRepository extends JpaRepository<ProjectAndProjectMember, Long> {
    @Query("select ppm " +
            "from PROJECT_PROJECT_AND_PROJECT_MEMBER ppm " +
            "join ppm.projectMemberList pml " +
            "where pml.projectMemberDeleteStatus = 'N' " +
            "order by ppm.projectDeadline desc")
    List<ProjectAndProjectMember> findProjectAndProjectMember();

    @Query("select ppm " +
            "from PROJECT_PROJECT_AND_PROJECT_MEMBER ppm " +
            "join ppm.projectMemberList pml " +
            "where pml.projectMemberDeleteStatus = 'N' " +
            "and pml.employeeCode = ?1 " +
            "order by ppm.projectDeadline desc")
    List<ProjectAndProjectMember> findProjectAndProjectMemberByEmployeeCode(Long employeeCode);

    @Query("select ppm " +
            "from PROJECT_PROJECT_AND_PROJECT_MEMBER ppm " +
            "join ppm.projectMemberList pml " +
            "where pml.projectMemberDeleteStatus = 'N' " +
            "and pml.employeeCode in ?1 " +
            "order by ppm.projectDeadline desc")
    List<ProjectAndProjectMember> findProjectAndProjectMemberByDepartmentCode(List<Long> employeeCodeListByDepartmentCode);

    @Query("select ppm " +
            "from PROJECT_PROJECT_AND_PROJECT_MEMBER ppm " +
            "join ppm.projectMemberList pml " +
            "where pml.projectMemberDeleteStatus = 'N' " +
            "and ppm.projectTitle like concat('%', ?1, '%') " +
            "order by ppm.projectDeadline desc")
    List<ProjectAndProjectMember> findProjectAndProjectMemberBySearchValue(String searchValue);

    @Query("select ppm " +
            "from PROJECT_PROJECT_AND_PROJECT_MEMBER ppm " +
            "join ppm.projectMemberList pml " +
            "where pml.projectMemberDeleteStatus = 'N' " +
            "and pml.employeeCode = ?1 " +
            "and ppm.projectTitle like concat('%', ?2, '%') " +
            "order by ppm.projectDeadline desc")
    List<ProjectAndProjectMember> findProjectAndProjectMemberByEmployeeCodeAndSearchValue(Long employeeCode, String searchValue);

    @Query("select ppm " +
            "from PROJECT_PROJECT_AND_PROJECT_MEMBER ppm " +
            "join ppm.projectMemberList pml " +
            "where pml.projectMemberDeleteStatus = 'N' " +
            "and pml.employeeCode in ?1 " +
            "and ppm.projectTitle like concat('%', ?2, '%') " +
            "order by ppm.projectDeadline desc")
    List<ProjectAndProjectMember> findProjectAndProjectMemberByDepartmentCodeAndSearchValue(List<Long> employeeCodeListByDepartmentCode, String searchValue);
}
