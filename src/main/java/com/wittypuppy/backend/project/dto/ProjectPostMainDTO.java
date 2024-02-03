package com.wittypuppy.backend.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

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
    private LocalDateTime projectPostCreationDate;
    private LocalDateTime projectPostModifyDate;
    private LocalDateTime projectPostDueDate;

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

    public ProjectPostMainDTO setProjectPostCreationDate(LocalDateTime projectPostCreationDate) {
        this.projectPostCreationDate = projectPostCreationDate;
        return this;
    }

    public ProjectPostMainDTO setProjectPostModifyDate(LocalDateTime projectPostModifyDate) {
        this.projectPostModifyDate = projectPostModifyDate;
        return this;
    }

    public ProjectPostMainDTO setProjectPostDueDate(LocalDateTime projectPostDueDate) {
        this.projectPostDueDate = projectPostDueDate;
        return this;
    }

    public ProjectPostMainDTO builder() {
        return new ProjectPostMainDTO(projectPostCode, projectCode, projectPostStatus, projectPostPriority, projectPostTitle, projectPostCreationDate, projectPostModifyDate, projectPostDueDate);
    }
}
