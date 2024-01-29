package com.wittypuppy.backend.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
@Entity(name = "PROJECT_PROJECT_POST")
@Table(name = "tbl_project_post")
public class ProjectPost {
    @Id
    @Column(name = "project_post_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectPostCode;

    @Column(name = "project_code")
    private Long projectCode;

    @Column(name = "project_post_status")
    private String projectPostStatus;

    @Column(name = "project_post_priority")
    private String projectPostPriority;

    @Column(name = "project_post_title")
    private String projectPostTitle;

    @Column(name = "project_post_creation_date")
    private LocalDateTime projectPostCreationDate;

    @Column(name = "project_post_modify_date")
    private LocalDateTime projectPostModifyDate;

    @Column(name = "project_post_due_date")
    private LocalDateTime projectPostDueDate;

    @JoinColumn(name = "project_post_code")
    @OneToMany
    private List<ProjectPostMember> projectPostMemberList;

    @JoinColumn(name="project_post_code")
    @OneToMany
    private List<ProjectPostComment> projectPostCommentList;
}
