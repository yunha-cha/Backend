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
//@Entity(name = "PROJECT_PROJECT_POST")
//@Table(name = "tbl_project_post")
//public class ProjectPost {
//    @Id
//    @Column(name = "project_post_code", columnDefinition = "BIGINT")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long projectPostCode;
//
//    @Column(name = "project_code")
//    private Long projectCode;
//
//    @Column(name = "project_post_status")
//    private String projectPostStatus;
//
//    @Column(name = "project_post_priority")
//    private String projectPostPriority;
//
//    @Column(name = "project_post_title")
//    private String projectPostTitle;
//
//    @Column(name = "project_post_creation_date")
//    private Date projectPostCreationDate;
//
//    @Column(name = "project_post_modify_date")
//    private Date projectPostModifyDate;
//
//    @Column(name = "project_post_due_date")
//    private Date projectPostDueDate;
//
//    @JoinColumn(name = "project_post_code")
//    @OneToMany(cascade = CascadeType.PERSIST)
//    private List<ProjectPostMember> projectPostMemberList;
//
//    @JoinColumn(name = "project_post_code")
//    @OneToMany
//    private List<ProjectPostComment> projectPostCommentList;
//
//    public ProjectPost setProjectPostCode(Long projectPostCode) {
//        this.projectPostCode = projectPostCode;
//        return this;
//    }
//
//    public ProjectPost setProjectCode(Long projectCode) {
//        this.projectCode = projectCode;
//        return this;
//    }
//
//    public ProjectPost setProjectPostStatus(String projectPostStatus) {
//        this.projectPostStatus = projectPostStatus;
//        return this;
//    }
//
//    public ProjectPost setProjectPostPriority(String projectPostPriority) {
//        this.projectPostPriority = projectPostPriority;
//        return this;
//    }
//
//    public ProjectPost setProjectPostTitle(String projectPostTitle) {
//        this.projectPostTitle = projectPostTitle;
//        return this;
//    }
//
//    public ProjectPost setProjectPostCreationDate(Date projectPostCreationDate) {
//        this.projectPostCreationDate = projectPostCreationDate;
//        return this;
//    }
//
//    public ProjectPost setProjectPostModifyDate(Date projectPostModifyDate) {
//        this.projectPostModifyDate = projectPostModifyDate;
//        return this;
//    }
//
//    public ProjectPost setProjectPostDueDate(Date projectPostDueDate) {
//        this.projectPostDueDate = projectPostDueDate;
//        return this;
//    }
//
//    public ProjectPost setProjectPostMemberList(List<ProjectPostMember> projectPostMemberList) {
//        this.projectPostMemberList = projectPostMemberList;
//        return this;
//    }
//
//    public ProjectPost setProjectPostCommentList(List<ProjectPostComment> projectPostCommentList) {
//        this.projectPostCommentList = projectPostCommentList;
//        return this;
//    }
//
//    public ProjectPost builder() {
//        return new ProjectPost(projectPostCode, projectCode, projectPostStatus, projectPostPriority, projectPostTitle, projectPostCreationDate, projectPostModifyDate, projectPostDueDate, projectPostMemberList, projectPostCommentList);
//    }
//}
