package com.wittypuppy.backend.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class ProjectDTO {
    private Long projectCode;
    private String projectTitle;
    private String projectDescription;
    private Date projectDeadline;
    private String projectLockedStatus;
    private String projectProgressStatus;

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

    public ProjectDTO setProjectDeadline(Date projectDeadline) {
        this.projectDeadline = projectDeadline;
        return this;
    }

    public ProjectDTO setProjectLockedStatus(String projectLockedStatus) {
        this.projectLockedStatus = projectLockedStatus;
        return this;
    }

    public ProjectDTO setProjectProgressStatus(String projectProgressStatus) {
        this.projectProgressStatus = projectProgressStatus;
        return this;
    }

    public ProjectDTO builder() {
        return new ProjectDTO(projectCode, projectTitle, projectDescription, projectDeadline, projectLockedStatus,projectProgressStatus);
    }
}
