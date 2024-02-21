//package com.wittypuppy.backend.project.repository;
//
//import com.wittypuppy.backend.project.dto.ProjectPostCommentFileDTO;
//import com.wittypuppy.backend.project.entity.ProjectPostCommentFile;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository("Project_ProjectPostCommentFileRepository")
//public interface ProjectPostCommentFileRepository extends JpaRepository<ProjectPostCommentFile, Long> {
//
//    @Query(value =
//            "SELECT " +
//                    "tppc.project_post_comment_code," +
//                    "tppcf.project_post_comment_file_code," +
//                    "tppcf.project_post_comment_file_changed_file," +
//                    "tppcf.project_post_comment_file_creation_date " +
//                    "FROM tbl_project_post_comment tppc " +
//                    "LEFT JOIN tbl_project_post_comment_file tppcf ON tppc.project_post_comment_code=tppcf.project_post_comment_code " +
//                    "WHERE tppc.project_post_comment_code=:projectPostCommentCode",
//            nativeQuery = true)
//    List<ProjectPostCommentFileDTO> selectProjectPostCommentFileList(Long projectPostCommentCode);
//}
