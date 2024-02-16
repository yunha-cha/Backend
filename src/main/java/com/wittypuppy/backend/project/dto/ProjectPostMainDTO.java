package com.wittypuppy.backend.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class ProjectPostMainDTO {
    private Long projectPostCode;
    private Long projectCode;
    private String projectPostStatus;
    private String projectPostPriority;
    private String projectPostTitle;
    private Date projectPostCreationDate;
    private Date projectPostModifyDate;
    private Date projectPostDueDate;

    public ProjectPostMainDTO setProjectPostCode(Long projectPostCode) {
        this.projectPostCode = projectPostCode;
        return this;
    }

    public ProjectPostMainDTO setProjectCode(Long projectCode) {
        this.projectCode = projectCode;
        return this;
    }

    public ProjectPostMainDTO setProjectPostStatus(String projectPostStatus) {
        this.projectPostStatus = projectPostStatus;
        return this;
    }

    public ProjectPostMainDTO setProjectPostPriority(String projectPostPriority) {
        this.projectPostPriority = projectPostPriority;
        return this;
    }

    public ProjectPostMainDTO setProjectPostTitle(String projectPostTitle) {
        this.projectPostTitle = projectPostTitle;
        return this;
    }

    public ProjectPostMainDTO setProjectPostCreationDate(Date projectPostCreationDate) {
        this.projectPostCreationDate = projectPostCreationDate;
        return this;
    }

    public ProjectPostMainDTO setProjectPostModifyDate(Date projectPostModifyDate) {
        this.projectPostModifyDate = projectPostModifyDate;
        return this;
    }

    public ProjectPostMainDTO setProjectPostDueDate(Date projectPostDueDate) {
        this.projectPostDueDate = projectPostDueDate;
        return this;
    }

    public ProjectPostMainDTO builder() {
        return new ProjectPostMainDTO(projectPostCode, projectCode, projectPostStatus, projectPostPriority, projectPostTitle, projectPostCreationDate, projectPostModifyDate, projectPostDueDate);
    }
}
