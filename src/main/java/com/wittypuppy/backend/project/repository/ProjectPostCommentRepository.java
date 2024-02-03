package com.wittypuppy.backend.project.repository;

import com.wittypuppy.backend.project.dto.ProjectPostCommentDTO;
import com.wittypuppy.backend.project.entity.ProjectPostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("Project_ProjectPostCommentRepository")
public interface ProjectPostCommentRepository extends JpaRepository<ProjectPostComment, Long> {
    List<ProjectPostComment> findAllByProjectPostCode(Long projectPostCode);

    @Query(value = "SELECT " +
            "tppc.project_post_code," +
            "tppc.project_post_comment_content," +
            "tppm.project_post_member_code, " +
            "tpm.project_member_code," +
            "te.employee_code," +
            "te.employee_name," +
            "td.department_name," +
            "tj.job_name," +
            "tp.profile_changed_file," +
            "(SELECT tppcf.project_post_comment_file_changed_file FROM tbl_project_post_comment_file tppcf WHERE tppcf.project_post_comment_code=tppc.project_post_comment_code LIMIT 1), " +
            "(SELECT COUNT(*) FROM tbl_project_post_comment_file tppcf WHERE tppcf.project_post_comment_code=tppc.project_post_comment_code) " +
            "FROM tbl_project_post_comment tppc " +
            "LEFT JOIN tbl_project_post_member tppm ON tppc.project_post_member_code = tppm.project_post_member_code " +
            "LEFT JOIN tbl_project_member tpm ON tppm.project_member_code = tpm.project_member_code " +
            "LEFT JOIN tbl_employee te ON tpm.employee_code = te.employee_code " +
            "LEFT JOIN tbl_department td ON te.department_code = td.department_code " +
            "LEFT JOIN tbl_job tj ON te.job_code = tj.job_code " +
            "LEFT JOIN tbl_profile tp ON te.employee_code = tp.employee_code " +
            "WHERE tp.profile_delete_status='N'"
            , nativeQuery = true)
    List<ProjectPostCommentDTO> selectProjectPostCommentList(Long projectPostCode);
}