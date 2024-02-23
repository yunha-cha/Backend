package com.wittypuppy.backend.project.repository;

import com.wittypuppy.backend.project.dto.ProjectMainInterface;
import com.wittypuppy.backend.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("Project_ProjectRepository")
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query(value = "SELECT tp.project_code projectCode, " +
            "te.employee_name projectManagerName," +
            "td.department_name projectManagerDeptName," +
            "tp.project_title projectTitle," +
            "tp.project_description projectDescription," +
            "tp.project_progress_status projectProgressStatus," +
            "tp.project_deadline projectDeadline," +
            "tp.project_locked_status projectLockedStatus, " +
            "(SELECT COUNT(*) FROM tbl_project_member tpm WHERE tpm.project_member_delete_status='N' AND tpm.project_code=tp.project_code) projectMemberCount ," +
            "(SELECT COUNT(*) FROM tbl_project_member tpm WHERE tpm.project_member_delete_status='N' AND tpm.project_code=tp.project_code AND tpm.employee_code = :employeeCode) myParticipationStatus " +
            "FROM tbl_project tp " +
            "LEFT JOIN tbl_employee te " +
            "ON tp.project_manager_code = te.employee_code " +
            "LEFT JOIN tbl_department td " +
            "ON te.department_code = td.department_code " +
            "ORDER BY tp.project_code DESC " +
            "LIMIT :startCount, :searchCount",
            nativeQuery = true)
    List<ProjectMainInterface> findAllProjectInfoWithPaging(Long employeeCode, Integer startCount, Integer searchCount);

    @Query(value = "SELECT count(*) " +
            "FROM tbl_project tp " +
            "LEFT JOIN tbl_employee te " +
            "ON tp.project_manager_code = te.employee_code " +
            "LEFT JOIN tbl_department td " +
            "ON te.department_code = td.department_code ",
            nativeQuery = true)
    Long getCountAllProject();

    @Query(value = "SELECT tp.project_code projectCode, " +
            "te.employee_name projectManagerName," +
            "td.department_name projectManagerDeptName," +
            "tp.project_title projectTitle," +
            "tp.project_description projectDescription," +
            "tp.project_progress_status projectProgressStatus," +
            "tp.project_deadline projectDeadline," +
            "tp.project_locked_status projectLockedStatus, " +
            "(SELECT COUNT(*) FROM tbl_project_member WHERE project_member_delete_status='N' AND project_code=tp.project_code) projectMemberCount, " +
            "(SELECT COUNT(*) FROM tbl_project_member tpm WHERE tpm.project_member_delete_status='N' AND tpm.project_code=tp.project_code AND tpm.employee_code = :employeeCode) myParticipationStatus " +
            "FROM tbl_project tp " +
            "LEFT JOIN tbl_employee te " +
            "ON tp.project_manager_code = te.employee_code " +
            "LEFT JOIN tbl_department td " +
            "ON te.department_code = td.department_code " +
            "LEFT JOIN tbl_project_member tpm " +
            "ON tp.project_code = tpm.project_code " +
            "WHERE tpm.employee_code = :employeeCode " +
            "AND tpm.project_member_delete_status = 'N' " +
            "ORDER BY tp.project_code DESC " +
            "LIMIT :startCount, :searchCount",
            nativeQuery = true)
    List<ProjectMainInterface> findMyProjectInfoWithPaging(Long employeeCode, Integer startCount, Integer searchCount);

    @Query(value = "SELECT count(*) " +
            "FROM tbl_project tp " +
            "LEFT JOIN tbl_employee te " +
            "ON tp.project_manager_code = te.employee_code " +
            "LEFT JOIN tbl_department td " +
            "ON te.department_code = td.department_code " +
            "LEFT JOIN tbl_project_member tpm " +
            "ON tp.project_code = tpm.project_code " +
            "WHERE tpm.employee_code = :employeeCode " +
            "AND tpm.project_member_delete_status = 'N' ",
            nativeQuery = true)
    Long getCountMyProject(Long employeeCode);

    @Query(value = "SELECT tp.project_code projectCode, " +
            "te.employee_name projectManagerName," +
            "td.department_name projectManagerDeptName," +
            "tp.project_title projectTitle," +
            "tp.project_description projectDescription," +
            "tp.project_progress_status projectProgressStatus," +
            "tp.project_deadline projectDeadline," +
            "tp.project_locked_status projectLockedStatus, " +
            "(SELECT COUNT(*) FROM tbl_project_member WHERE project_member_delete_status='N' AND project_code=tp.project_code) projectMemberCount,  " +
            "(SELECT COUNT(*) FROM tbl_project_member tpm WHERE tpm.project_member_delete_status='N' AND tpm.project_code=tp.project_code AND tpm.employee_code = :employeeCode) myParticipationStatus " +
            "FROM tbl_project tp " +
            "LEFT JOIN tbl_employee te " +
            "ON tp.project_manager_code = te.employee_code " +
            "LEFT JOIN tbl_department td " +
            "ON te.department_code = td.department_code " +
            "LEFT JOIN tbl_project_member tpm " +
            "ON tp.project_code = tpm.project_code " +
            "WHERE " +
            "(SELECT subTD.department_code " +
            "FROM tbl_employee subTE " +
            "LEFT JOIN tbl_department subTD ON subTD.department_code = subTE.department_code " +
            "WHERE subTE.employee_code = tpm.employee_code) = :deptCode " +
            "AND tpm.employee_code = :employeeCode " +
            "AND tpm.project_member_delete_status = 'N' " +
            "ORDER BY tp.project_code DESC " +
            "LIMIT :startCount, :searchCount",
            nativeQuery = true)
    List<ProjectMainInterface> findMyDeptProjectInfoWithPaging(Long deptCode, Long employeeCode, Integer startCount, Integer searchCount);

    @Query(value = "SELECT count(*) " +
            "FROM tbl_project tp " +
            "LEFT JOIN tbl_employee te " +
            "ON tp.project_manager_code = te.employee_code " +
            "LEFT JOIN tbl_department td " +
            "ON te.department_code = td.department_code " +
            "LEFT JOIN tbl_project_member tpm " +
            "ON tp.project_code = tpm.project_code " +
            "WHERE " +
            "(SELECT subTD.department_code " +
            "FROM tbl_employee subTE " +
            "LEFT JOIN tbl_department subTD ON subTD.department_code = subTE.department_code " +
            "WHERE subTE.employee_code = tpm.employee_code) = :deptCode " +
            "AND tpm.employee_code = :employeeCode " +
            "AND tpm.project_member_delete_status = 'N' ",
            nativeQuery = true)
    Long getCountMyDeptProject(Long deptCode, Long employeeCode);

    @Query(value = "SELECT tp.project_code projectCode, " +
            "te.employee_name projectManagerName," +
            "td.department_name projectManagerDeptName," +
            "tp.project_title projectTitle," +
            "tp.project_description projectDescription," +
            "tp.project_progress_status projectProgressStatus," +
            "tp.project_deadline projectDeadline," +
            "tp.project_locked_status projectLockedStatus, " +
            "(SELECT COUNT(*) FROM tbl_project_member tpm WHERE tpm.project_member_delete_status='N' AND tpm.project_code=tp.project_code) projectMemberCount, " +
            "(SELECT COUNT(*) FROM tbl_project_member tpm WHERE tpm.project_member_delete_status='N' AND tpm.project_code=tp.project_code AND tpm.employee_code = :employeeCode) myParticipationStatus " +
            "FROM tbl_project tp " +
            "LEFT JOIN tbl_employee te " +
            "ON tp.project_manager_code = te.employee_code " +
            "LEFT JOIN tbl_department td " +
            "ON te.department_code = td.department_code " +
            "WHERE tp.project_title LIKE concat('%',:searchValue,'%') " +
            "ORDER BY tp.project_code DESC " +
            "LIMIT :startCount, :searchCount",
            nativeQuery = true)
    List<ProjectMainInterface> searchAllProjectInfoWithPaging(Long employeeCode, String searchValue, Integer startCount, Integer searchCount);

    @Query(value = "SELECT count(*) " +
            "FROM tbl_project tp " +
            "LEFT JOIN tbl_employee te " +
            "ON tp.project_manager_code = te.employee_code " +
            "LEFT JOIN tbl_department td " +
            "ON te.department_code = td.department_code " +
            "WHERE tp.project_title LIKE concat('%',:searchValue,'%') ",
            nativeQuery = true)
    Long getSearchCountAllProject(String searchValue);

    @Query(value = "SELECT tp.project_code projectCode, " +
            "te.employee_name projectManagerName," +
            "td.department_name projectManagerDeptName," +
            "tp.project_title projectTitle," +
            "tp.project_description projectDescription," +
            "tp.project_progress_status projectProgressStatus," +
            "tp.project_deadline projectDeadline," +
            "tp.project_locked_status projectLockedStatus, " +
            "(SELECT COUNT(*) FROM tbl_project_member WHERE project_member_delete_status='N' AND project_code=tp.project_code) projectMemberCount, " +
            "(SELECT COUNT(*) FROM tbl_project_member tpm WHERE tpm.project_member_delete_status='N' AND tpm.project_code=tp.project_code AND tpm.employee_code = :employeeCode) myParticipationStatus " +
            "FROM tbl_project tp " +
            "LEFT JOIN tbl_employee te " +
            "ON tp.project_manager_code = te.employee_code " +
            "LEFT JOIN tbl_department td " +
            "ON te.department_code = td.department_code " +
            "LEFT JOIN tbl_project_member tpm " +
            "ON tp.project_code = tpm.project_code " +
            "WHERE tpm.employee_code = :employeeCode " +
            "AND tp.project_title LIKE concat('%',:searchValue,'%') " +
            "AND tpm.project_member_delete_status = 'N' " +
            "ORDER BY tp.project_code DESC " +
            "LIMIT :startCount, :searchCount",
            nativeQuery = true)
    List<ProjectMainInterface> searchMyProjectInfoWithPaging(Long employeeCode, String searchValue, Integer startCount, Integer searchCount);

    @Query(value = "SELECT count(*) " +
            "FROM tbl_project tp " +
            "LEFT JOIN tbl_employee te " +
            "ON tp.project_manager_code = te.employee_code " +
            "LEFT JOIN tbl_department td " +
            "ON te.department_code = td.department_code " +
            "LEFT JOIN tbl_project_member tpm " +
            "ON tp.project_code = tpm.project_code " +
            "WHERE tpm.employee_code = :employeeCode " +
            "AND tp.project_title LIKE concat('%',:searchValue,'%') "+
            "AND tpm.project_member_delete_status = 'N' " ,
            nativeQuery = true)
    Long getSearchCountMyProject(Long employeeCode, String searchValue);

    @Query(value = "SELECT tp.project_code projectCode, " +
            "te.employee_name projectManagerName," +
            "td.department_name projectManagerDeptName," +
            "tp.project_title projectTitle," +
            "tp.project_description projectDescription," +
            "tp.project_progress_status projectProgressStatus," +
            "tp.project_deadline projectDeadline," +
            "tp.project_locked_status projectLockedStatus, " +
            "(SELECT COUNT(*) FROM tbl_project_member WHERE project_member_delete_status='N' AND project_code=tp.project_code) projectMemberCount, " +
            "(SELECT COUNT(*) FROM tbl_project_member tpm WHERE tpm.project_member_delete_status='N' AND tpm.project_code=tp.project_code AND tpm.employee_code = :employeeCode) myParticipationStatus " +
            "FROM tbl_project tp " +
            "LEFT JOIN tbl_employee te " +
            "ON tp.project_manager_code = te.employee_code " +
            "LEFT JOIN tbl_department td " +
            "ON te.department_code = td.department_code " +
            "LEFT JOIN tbl_project_member tpm " +
            "ON tp.project_code = tpm.project_code " +
            "WHERE " +
            "(SELECT subTD.department_code " +
            "FROM tbl_employee subTE " +
            "LEFT JOIN tbl_department subTD ON subTD.department_code = subTE.department_code " +
            "WHERE subTE.employee_code = tpm.employee_code) = :deptCode " +
            "AND tp.project_title LIKE concat('%',:searchValue,'%') " +
            "AND tpm.employee_code = :employeeCode " +
            "AND tpm.project_member_delete_status = 'N' " +
            "ORDER BY tp.project_code DESC " +
            "LIMIT :startCount, :searchCount",
            nativeQuery = true)
    List<ProjectMainInterface> searchMyDeptProjectInfoWithPaging(Long deptCode, Long employeeCode, String searchValue, Integer startCount, Integer searchCount);

    @Query(value = "SELECT count(*) " +
            "FROM tbl_project tp " +
            "LEFT JOIN tbl_employee te " +
            "ON tp.project_manager_code = te.employee_code " +
            "LEFT JOIN tbl_department td " +
            "ON te.department_code = td.department_code " +
            "LEFT JOIN tbl_project_member tpm " +
            "ON tp.project_code = tpm.project_code " +
            "WHERE " +
            "(SELECT subTD.department_code " +
            "FROM tbl_employee subTE " +
            "LEFT JOIN tbl_department subTD ON subTD.department_code = subTE.department_code " +
            "WHERE subTE.employee_code = tpm.employee_code) = :deptCode " +
            "AND tp.project_title LIKE concat('%',:searchValue,'%') " +
            "AND tpm.employee_code = :employeeCode " +
            "AND tpm.project_member_delete_status = 'N' ",
            nativeQuery = true)
    Long getSearchCountMyDeptProject(Long deptCode, Long employeeCode, String searchValue);
}