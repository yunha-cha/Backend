package com.wittypuppy.backend.project.repository;

import com.wittypuppy.backend.project.dto.ProjectPostMemberDTO;
import com.wittypuppy.backend.project.entity.ProjectPostMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("Project_ProjectPostMemberRepository")
public interface ProjectPostMemberRepository extends JpaRepository<ProjectPostMember, Long> {
    List<ProjectPostMember> findAllByProjectPostCodeAndProjectPostMemberDeleteStatus(Long projectPostCode, String projectPostMemberDeleteStatus);

    Optional<ProjectPostMember> findByProjectPostMemberCodeAndProjectPostMemberDeleteStatus(Long projectPostMemberCode, String projectPostMemberDeleteStatus);

    Optional<ProjectPostMember> findByProjectPostCodeAndProjectMemberCodeAndProjectPostMemberDeleteStatus(Long projectPostCode, Long projectMemberCode, String projectPostMemberDeleteStatus);

    @Query(
            value =
                    "SELECT " +
                            "tppm.project_post_member_code," +
                            "tppm.project_post_code," +
                            "tppm.project_member_code," +
                            "tppm.project_post_member_delete_status," +
                            "te.employee_code," +
                            "te.employee_name," +
                            "tp.profile_changed_file," +
                            "td.department_name," +
                            "tj.job_name " +
                            "FROM tbl_project_post_member tppm " +
                            "LEFT JOIN tbl_project_member tpm ON tppm.project_member_code = tpm.project_member_code " +
                            "LEFT JOIN tbl_employee te ON tpm.employee_code = te.employee_code " +
                            "LEFT JOIN tbl_department td ON te.department_code = td.department_code " +
                            "LEFT JOIN tbl_job tj ON te.job_code = tj.job_code " +
                            "LEFT JOIN tbl_profile tp ON te.employee_code = tp.employee_code " +
                            "LEFT JOIN tbl_project_post tpp ON tppm.project_post_code = tpp.project_post_code " +
                            "WHERE tpp.project_post_code=:projectPostCode " +
                            "AND tp.profile_delete_status='N'"
            , nativeQuery = true
    )
    List<ProjectPostMemberDTO> selectProjectPostMember(Long projectPostCode);
}