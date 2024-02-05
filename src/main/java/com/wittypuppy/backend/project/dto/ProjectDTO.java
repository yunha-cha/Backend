package com.wittypuppy.backend.project.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class ProjectDTO {
    private Long projectCode;
    private String projectTitle;
    private String projectDescription;
    private LocalDateTime projectDeadline;
    private String projectLockedStatus;

    public ProjectDTO setProjectCode(Long projectCode) {
        this.projectCode = projectCode;
        return this;
    }

    public ProjectDTO setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
        return this;
    }

    public ProjectDTO setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
        return this;
    }

    public ProjectDTO setProjectDeadline(LocalDateTime projectDeadline) {
        this.projectDeadline = projectDeadline;
        return this;
    }

    public ProjectDTO setProjectLockedStatus(String projectLockedStatus) {
        this.projectLockedStatus = projectLockedStatus;
        return this;
    }

    public ProjectDTO builder() {
        return new ProjectDTO(projectCode, projectTitle, projectDescription, projectDeadline, projectLockedStatus);
    }
}
