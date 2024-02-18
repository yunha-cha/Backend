package com.wittypuppy.backend.project.repository;

import com.wittypuppy.backend.project.dto.ProjectPostDTO;
import com.wittypuppy.backend.project.dto.ProjectPostInterface;
import com.wittypuppy.backend.project.entity.ProjectPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("Project_ProjectPostRepository")
public interface ProjectPostRepository extends JpaRepository<ProjectPost, Long> {
    List<ProjectPost> findAllByProjectCode(Long projectCode);

    @Query(value =
            "SELECT tpp.project_code," +
                    "tpp.project_post_code," +
                    "tpp.project_post_status," +
                    "tpp.project_post_priority," +
                    "tpp.project_post_title," +
                    "tpp.project_post_creation_date," +
                    "tpp.project_post_modify_date," +
                    "tpp.project_post_due_date," +
                    "(SELECT employee_name FROM tbl_employee WHERE employee_code = tpm.employee_code limit 1)," +
                    "(SELECT department_name FROM tbl_department WHERE department_code = td.department_code limit 1)," +
                    "(SELECT COUNT(*) FROM tbl_project_post_member WHERE project_post_code=tpp.project_post_code) " +
                    "FROM tbl_project_post tpp " +
                    "LEFT JOIN tbl_project_post_member tppm ON tppm.project_post_code = tpp.project_post_code " +
                    "LEFT JOIN tbl_project_member tpm ON tpm.project_member_code=tppm.project_member_code " +
                    "LEFT JOIN tbl_employee te ON te.employee_code = tpm.employee_code " +
                    "LEFT JOIN tbl_department td ON td.department_code = te.department_code " +
                    "WHERE tpm.project_code=:projectCode " +
                    "AND tpp.project_post_title LIKE concat('%',:searchValue,'%') " +
                    "ORDER BY tpp.project_code DESC " +
                    "LIMIT :startCount, :searchCount",
            nativeQuery = true)
    List<ProjectPostInterface> selectProjectPostListWithPaging(String searchValue, Long projectCode, Integer startCount, Integer searchCount);

    @Query(value =
            "SELECT count(*) " +
                    "FROM tbl_project_post tpp " +
                    "LEFT JOIN tbl_project_post_member tppm ON tppm.project_post_code = tpp.project_post_code " +
                    "LEFT JOIN tbl_project_member tpm ON tpm.project_member_code=tppm.project_member_code " +
                    "LEFT JOIN tbl_employee te ON te.employee_code = tpm.employee_code " +
                    "LEFT JOIN tbl_department td ON td.department_code = te.department_code " +
                    "WHERE tpm.project_code=:projectCode " +
                    "AND tpp.project_post_title LIKE concat('%',:searchValue,'%') " ,
            nativeQuery = true)
    Long getCountAllProjectPost(String searchValue, Long projectCode);
}