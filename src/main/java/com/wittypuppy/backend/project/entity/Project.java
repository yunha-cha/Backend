package com.wittypuppy.backend.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
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
    private Date projectDeadline;

    @Column(name = "project_locked_status")
    private String projectLockedStatus;

    @JoinColumn(name = "project_code")
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<ProjectMember> projectMemberList;

    @JoinColumn(name = "project_code")
    @OneToMany
    private List<ProjectPost> projectPostList;

    public Project setProjectCode(Long projectCode) {
        this.projectCode = projectCode;
        return this;
    }

    public Project setProjectManager(Employee projectManager) {
        this.projectManager = projectManager;
        return this;
    }

    public Project setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
        return this;
    }

    public Project setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
        return this;
    }

    public Project setProjectProgressStatus(String projectProgressStatus) {
        this.projectProgressStatus = projectProgressStatus;
        return this;
    }

    public Project setProjectDeadline(Date projectDeadline) {
        this.projectDeadline = projectDeadline;
        return this;
    }

    public Project setProjectLockedStatus(String projectLockedStatus) {
        this.projectLockedStatus = projectLockedStatus;
        return this;
    }

    public Project setProjectMemberList(List<ProjectMember> projectMemberList) {
        this.projectMemberList = projectMemberList;
        return this;
    }

    public Project setProjectPostList(List<ProjectPost> projectPostList) {
        this.projectPostList = projectPostList;
        return this;
    }

    public Project builder() {
        return new Project(projectCode, projectManager, projectTitle, projectDescription, projectProgressStatus, projectDeadline, projectLockedStatus, projectMemberList, projectPostList);
    }
}
