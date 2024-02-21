//package com.wittypuppy.backend.project.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.ToString;
//
//import java.time.LocalDateTime;
//import java.util.Date;
//import java.util.List;
//
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@ToString
//@Entity(name = "PROJECT_PROJECT_POST_COMMENT")
//@Table(name = "tbl_project_post_comment")
//public class ProjectPostComment {
//    @Id
//    @Column(name = "project_post_comment_code", columnDefinition = "BIGINT")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long projectPostCommentCode;
//
//    @Column(name = "project_post_code")
//    private Long projectPostCode;
//
//    @Column(name = "project_post_comment_content")
//    private String projectPostCommentContent;
//
//    @Column(name = "project_post_comment_creation_date")
//    private Date projectPostCommentCreationDate;
//
//    @Column(name = "project_post_member_code")
//    private Long projectPostMemberCode;
//
//    @JoinColumn(name = "project_post_comment_code")
//    @OneToMany
//    private List<ProjectPostCommentFile> projectPostCommentFileList;
//
//    public ProjectPostComment setProjectPostCommentCode(Long projectPostCommentCode) {
//        this.projectPostCommentCode = projectPostCommentCode;
//        return this;
//    }
//
//    public ProjectPostComment setProjectPostCode(Long projectPostCode) {
//        this.projectPostCode = projectPostCode;
//        return this;
//    }
//
//    public ProjectPostComment setProjectPostCommentContent(String projectPostCommentContent) {
//        this.projectPostCommentContent = projectPostCommentContent;
//        return this;
//    }
//
//    public ProjectPostComment setProjectPostCommentCreationDate(Date projectPostCommentCreationDate) {
//        this.projectPostCommentCreationDate = projectPostCommentCreationDate;
//        return this;
//    }
//
//    public ProjectPostComment setProjectPostMemberCode(Long projectPostMemberCode) {
//        this.projectPostMemberCode = projectPostMemberCode;
//        return this;
//    }
//
//    public ProjectPostComment setProjectPostCommentFileList(List<ProjectPostCommentFile> projectPostCommentFileList) {
//        this.projectPostCommentFileList = projectPostCommentFileList;
//        return this;
//    }
//
//    public ProjectPostComment builder() {
//        return new ProjectPostComment(projectPostCommentCode, projectPostCode, projectPostCommentContent, projectPostCommentCreationDate, projectPostMemberCode, projectPostCommentFileList);
//    }
//}
