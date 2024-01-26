package com.wittypuppy.backend.project.entity;

import com.wittypuppy.backend.common.entity.ProjectPostComment;
import com.wittypuppy.backend.common.entity.ProjectPostMember;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tbl_project_post")
public class ProjectPost {
    @Id
    @Column(name = "project_post_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectPostCode;

    @Column(name = "project_code", columnDefinition = "BIGINT")
    private Long projectCode;

    @Column(name = "project_post_status", columnDefinition = "VARCHAR(100)")
    private String projectPostStatus;

    @Column(name = "project_post_priority", columnDefinition = "VARCHAR(100)")
    private String projectPostPriority;

    @Column(name = "project_post_title", columnDefinition = "VARCHAR(100)")
    private String projectPostTitle;

    @Column(name = "project_post_creation_date", columnDefinition = "DATETIME")
    private LocalDateTime projectPostCreationDate;

    @Column(name = "project_post_modify_date", columnDefinition = "DATETIME")
    private LocalDateTime projectPostModifyDate;

    @Column(name = "project_post_due_date", columnDefinition = "DATETIME")
    private LocalDateTime projectPostDueDate;
}
