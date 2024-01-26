package com.wittypuppy.backend.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "PROJECT_PROJECT")
@Table(name = "tbl_project")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Project {
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

    public Project setProjectCode(Long projectCode) {
        this.projectCode = projectCode;
        return this;
    }

    public Project setProjectManagerCode(Long projectManagerCode) {
        this.projectManagerCode = projectManagerCode;
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

    public Project setProjectDeadline(LocalDateTime projectDeadline) {
        this.projectDeadline = projectDeadline;
        return this;
    }

    public Project setProjectLockedStatus(String projectLockedStatus) {
        this.projectLockedStatus = projectLockedStatus;
        return this;
    }

    public Project build() {
        return new Project(projectCode,
                projectManagerCode,
                projectTitle,
                projectDescription,
                projectProgressStatus,
                projectDeadline,
                projectLockedStatus);
    }
}
