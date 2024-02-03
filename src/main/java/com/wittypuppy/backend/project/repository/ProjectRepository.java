package com.wittypuppy.backend.project.repository;

import com.wittypuppy.backend.project.dto.ProjectMainDTO;
import com.wittypuppy.backend.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query(value = "SELECT tp.project_code projectCode, " +
            "te.employee_name projectManagerName," +
            "td.department_name projectManagerDeptName," +
            "tp.project_title projectTitle," +
            "tp.project_description projectDescription," +
            "tp.project_progress_status projectProgressStatus," +
            "tp.project_deadline projectDeadline," +
            "tp.project_locked_status projectLockedStatus, " +
            "(SELECT COUNT(*) FROM tbl_project_member tpm WHERE tpm.project_member_delete_status='N' AND tpm.project_code=tp.project_code) projectMemberCount " +
            "FROM tbl_project tp " +
            "LEFT JOIN tbl_employee te " +
            "ON tp.project_manager_code = te.employee_code " +
            "LEFT JOIN tbl_department td " +
            "ON te.department_code = td.department_code",
            nativeQuery = true)
    List<ProjectMainDTO> findAllProjectInfo();

    @Query(value = "SELECT tp.project_code projectCode, " +
            "te.employee_name projectManagerName," +
            "td.department_name projectManagerDeptName," +
            "tp.project_title projectTitle," +
            "tp.project_description projectDescription," +
            "tp.project_progress_status projectProgressStatus," +
            "tp.project_deadline projectDeadline," +
            "tp.project_locked_status projectLockedStatus, " +
            "(SELECT COUNT(*) FROM tbl_project_member WHERE project_member_delete_status='N' AND project_code=tp.project_code) projectMemberCount " +
            "FROM tbl_project tp " +
            "LEFT JOIN tbl_employee te " +
            "ON tp.project_manager_code = te.employee_code " +
            "LEFT JOIN tbl_department td " +
            "ON te.department_code = td.department_code " +
            "LEFT JOIN tbl_project_member tpm " +
            "ON tp.project_code = tpm.project_code " +
            "WHERE tpm.employee_code = :employeeCode",
            nativeQuery = true)
    List<ProjectMainDTO> findMyProjectInfo(Long employeeCode);

    @Query(value = "SELECT tp.project_code projectCode, " +
            "te.employee_name projectManagerName," +
            "td.department_name projectManagerDeptName," +
            "tp.project_title projectTitle," +
            "tp.project_description projectDescription," +
            "tp.project_progress_status projectProgressStatus," +
            "tp.project_deadline projectDeadline," +
            "tp.project_locked_status projectLockedStatus, " +
            "(SELECT COUNT(*) FROM tbl_project_member WHERE project_member_delete_status='N' AND project_code=tp.project_code) projectMemberCount " +
            "FROM tbl_project tp " +
            "LEFT JOIN tbl_employee te " +
            "ON tp.project_manager_code = te.employee_code " +
            "LEFT JOIN tbl_department td " +
            "ON te.department_code = td.department_code " +
            "LEFT JOIN tbl_project_member tpm " +
            "ON tp.project_code = tpm.project_code " +
            "WHERE te.department_code = :deptCode",
            nativeQuery = true)
    List<ProjectMainDTO> findMyDeptProjectInfo(Long deptCode);

    @Query(value = "SELECT tp.project_code projectCode, " +
            "te.employee_name projectManagerName," +
            "td.department_name projectManagerDeptName," +
            "tp.project_title projectTitle," +
            "tp.project_description projectDescription," +
            "tp.project_progress_status projectProgressStatus," +
            "tp.project_deadline projectDeadline," +
            "tp.project_locked_status projectLockedStatus, " +
            "(SELECT COUNT(*) FROM tbl_project_member tpm WHERE tpm.project_member_delete_status='N' AND tpm.project_code=tp.project_code) projectMemberCount " +
            "FROM tbl_project tp " +
            "LEFT JOIN tbl_employee te " +
            "ON tp.project_manager_code = te.employee_code " +
            "LEFT JOIN tbl_department td " +
            "ON te.department_code = td.department_code " +
            "WHERE tp.project_title=concat('%',:searchValue,'%')",
            nativeQuery = true)
    List<ProjectMainDTO> searchAllProjectInfo(String searchValue);

    @Query(value = "SELECT tp.project_code projectCode, " +
            "te.employee_name projectManagerName," +
            "td.department_name projectManagerDeptName," +
            "tp.project_title projectTitle," +
            "tp.project_description projectDescription," +
            "tp.project_progress_status projectProgressStatus," +
            "tp.project_deadline projectDeadline," +
            "tp.project_locked_status projectLockedStatus, " +
            "(SELECT COUNT(*) FROM tbl_project_member WHERE project_member_delete_status='N' AND project_code=tp.project_code) projectMemberCount " +
            "FROM tbl_project tp " +
            "LEFT JOIN tbl_employee te " +
            "ON tp.project_manager_code = te.employee_code " +
            "LEFT JOIN tbl_department td " +
            "ON te.department_code = td.department_code " +
            "LEFT JOIN tbl_project_member tpm " +
            "ON tp.project_code = tpm.project_code " +
            "WHERE tpm.employee_code = :employeeCode " +
            "AND tp.project_title=concat('%',:searchValue,'%')",
            nativeQuery = true)
    List<ProjectMainDTO> searchMyProjectInfo(Long employeeCode, String searchValue);

    @Query(value = "SELECT tp.project_code projectCode, " +
            "te.employee_name projectManagerName," +
            "td.department_name projectManagerDeptName," +
            "tp.project_title projectTitle," +
            "tp.project_description projectDescription," +
            "tp.project_progress_status projectProgressStatus," +
            "tp.project_deadline projectDeadline," +
            "tp.project_locked_status projectLockedStatus, " +
            "(SELECT COUNT(*) FROM tbl_project_member WHERE project_member_delete_status='N' AND project_code=tp.project_code) projectMemberCount " +
            "FROM tbl_project tp " +
            "LEFT JOIN tbl_employee te " +
            "ON tp.project_manager_code = te.employee_code " +
            "LEFT JOIN tbl_department td " +
            "ON te.department_code = td.department_code " +
            "LEFT JOIN tbl_project_member tpm " +
            "ON tp.project_code = tpm.project_code " +
            "WHERE te.department_code = :deptCode " +
            "AND tp.project_title=concat('%',:searchValue,'%')",
            nativeQuery = true)
    List<ProjectMainDTO> searchMyDeptProjectInfo(Long deptCode, String searchValue);
}