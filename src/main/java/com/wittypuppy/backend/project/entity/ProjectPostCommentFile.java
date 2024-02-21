//package com.wittypuppy.backend.project.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.time.LocalDateTime;
//import java.util.Date;
//
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@ToString
//@Setter
//@Entity(name = "PROJECT_PROJECT_POST_COMMENT_FILE")
//@Table(name = "tbl_project_post_comment_file")
//public class ProjectPostCommentFile {
//    @Id
//    @Column(name = "project_post_comment_file_code", columnDefinition = "BIGINT")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long projectPostCommentFileCode;
//
//    @Column(name = "project_post_comment_code")
//    private Long projectPostCommentCode;
//
//    @Column(name = "project_post_comment_file_og_file")
//    private String projectPostCommentFileOgFile;
//
//    @Column(name = "project_post_comment_file_changed_file")
//    private String projectPostCommentFileChangedFile;
//
//    @Column(name = "project_post_comment_file_creation_date")
//    private Date projectPostCommentFileCreationDate;
//}
