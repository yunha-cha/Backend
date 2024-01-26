package com.wittypuppy.backend.project.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "PROJECT_PROJECT_AND_MEMBER_AND_POST_AND_POST_MEMBER")
@Table(name = "tbl_project")
@Getter
public class ProjectAndMemberAndPostAndPostMember {
    @Id
    @Column(name = "project_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectCode;

    @Column(name = "project_manager_code", columnDefinition = "BIGINT")
    private Long projectManagerCode;

    @Column(name = "project_title", columnDefinition = "VARCHAR(100)")
    private String projectTitle;

    @Column(name = "project_description", columnDefinition = "VARCHAR(3000)")
    private String projectDescription;

    @Column(name = "project_progress_status", columnDefinition = "VARCHAR(100)")
    private String projectProgressStatus;

    @Column(name = "project_deadline", columnDefinition = "DATETIME")
    private LocalDateTime projectDeadline;

    @Column(name = "project_locked_status", columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String projectLockedStatus;

    @JoinColumn(name = "project_code")
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<ProjectPost> projectPostList;

    @JoinColumn(name = "project_code")
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<ProjectMemberAndPostMember> projectMemberAndPostMemberList;
}
