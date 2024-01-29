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
@Entity(name = "PROJECT_PROJECT")
@Table(name = "tbl_project")
public class Project {
    @Id
    @Column(name = "project_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectCode;

    @JoinColumn(name = "project_manager_code")
    @ManyToOne
    private Employee projectManager;

    @Column(name = "project_title")
    private String projectTitle;

    @Column(name = "project_description")
    private String projectDescription;

    @Column(name = "project_progress_status")
    private String projectProgressStatus;

    @Column(name = "project_deadline")
    private LocalDateTime projectDeadline;

    @Column(name = "project_locked_status")
    private String projectLockedStatus;

    @JoinColumn(name = "project_code")
    @OneToMany
    private List<ProjectMember> projectMemberList;

    @JoinColumn(name = "project_code")
    @OneToMany
    private List<ProjectPost> projectPostList;
}
