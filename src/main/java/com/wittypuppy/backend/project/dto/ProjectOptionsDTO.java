package com.wittypuppy.backend.project.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProjectOptionsDTO {
    private Long projectCode;
    private String projectTitle;
    private String projectDescription;
    private String progressStatus;
    private LocalDateTime projectDeadline;
    private String projectLockedStatus;
}
